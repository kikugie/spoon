package xd.arkosammy.monkeyconfig.tables

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.types.*
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * Represents a container of config settings useful for grouping related config settings under a common namespace
 * and for serializing using a [ConfigManager].
 * They can be registered with a [ConfigManager] to be loaded from and saved to a config file.
 */
interface ConfigTable {

    /**
     * The name of this config table.
     */
    val name: String

    /**
     * The comment for this config table to be written to the config file, if any.
     */
    val comment: String?

    /**
     * The list of [ConfigSetting] instances stored in this [ConfigTable]. This list should ideally be immutable.
     */
    val configSettings: List<ConfigSetting<*, *>>

    /**
     * Whether this table has been registered with a config manager.
     * This value should be set using [ConfigTable.setAsRegistered] after initializing the table
     * and adding it to a [ConfigManager] to prevent further settings from being added.
     */
    var isRegistered: Boolean

    /**
     * Whether this table should be loaded before being saved to file. This is useful for tables whose values should be updated in memory before being written to the config file again
     */
    val loadBeforeSave : Boolean

    /**
     * Whether the [ConfigSetting] instances should be used
     * to register commands for editing the values of those settings.
     * Should be `false` if the [ConfigTable]'s [ConfigSetting] instances can change during runtime,
     * should as with [MapConfigTable]
     */
    val registerSettingsAsCommands: Boolean

    /**
     * Sets this config table as registered. This method should be called after the table has been added to a config manager and should be used a check before attempting to modify the table.
     */
    fun setAsRegistered()

    /**
     * Called when and after the table is registered with a config manager.
     */
    fun onRegistered() {}

    /**
     * Called when and after the table is loaded from a config file.
     */
    fun onLoaded() {}

    /**
     * Called when and after the table is saved to a config file.
     */
    fun onSavedToFile() {}

    /**
     * Sets the values of the settings in this table to their default values, then writes these values to {@code fileConfig}.
     * This method should be called when creating a new config file or when the config file is missing settings.
     *
     * @param fileConfig The {@code CommentedFileConfig} instance to which to write the default values of the settings to
     */
    fun setDefaultValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            setting.resetValue()
        }
        this.setValues(fileConfig)
    }

    /**
     * Writes the current values of the settings in this table to {@code fileConfig}.
     *
     * @param fileConfig The [CommentedFileConfig] instance to which to write the values of the settings to
     */
    fun setValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            val settingAddress: String = "${setting.settingIdentifier.tableName}.${setting.settingIdentifier.settingName}"
            val valueAsSerialized: SerializableType<*> = setting.valueAsSerialized
            fileConfig.set<Any>(settingAddress, if(valueAsSerialized is ListType<*>) valueAsSerialized.listAsFullyDeserialized else valueAsSerialized.value)
            setting.comment?.let { comment ->
                if(fileConfig is CommentedFileConfig) fileConfig.setComment(settingAddress, comment)
            }
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
        val tableConfig: CommentedConfig = fileConfig.get(this.name)
        tableConfig.entrySet().removeIf { entry -> !this.containsSettingName(entry.key)}
    }

    /**
     * Loads the values of the settings in this table from {@code fileConfig}.
     *
     * @param fileConfig The [CommentedFileConfig] instance from which to load the values of the settings from
     */
    fun loadValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            val settingAddress: String = "${setting.settingIdentifier.tableName}.${setting.settingIdentifier.settingName}"
            val value: Any = (if(setting is EnumSetting<*>) fileConfig.getEnum(settingAddress, setting.enumClass) ?: setting.defaultValue else fileConfig.getOrElse(settingAddress, setting.defaultValueAsSerialized))!!
            val deserializedValue: SerializableType<*> = toSerializedType(value)
            setValueSafely(setting, deserializedValue)
        }
    }

    /**
     * Checks whether this table contains a setting with the name [settingName].
     * @return whether this table contains a setting with the name [settingName]
     */
    fun containsSettingName(settingName: String) : Boolean {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            if(setting.settingIdentifier.settingName == settingName) {
                return true
            }
        }
        return false
    }

}

fun toSerializedType(value: Any): SerializableType<*> {
    return when (value) {
        is SerializableType<*> -> value
        is List<*> -> ListType(value.filterNotNull().map { e -> toSerializedType(e) })
        is Number -> NumberType(value)
        is String -> StringType(value)
        is Boolean -> BooleanType(value)
        is Enum<*> -> EnumType(value)
        else -> throw IllegalArgumentException("Unsupported type: ${value::class.simpleName}")
    }
}

/**
 * Attempts to safely assign a [SerializableType] as the value of a [ConfigSetting] by checking the type of [value], and calling [ConfigSetting.setFromSerializedValue] accordingly.
 *
 * @param setting The setting to assign a [SerializableType] value to
 * @param value The [SerializableType] to assign as the value of the [setting]
 */
@Suppress("UNCHECKED_CAST")
fun <T, V : SerializableType<*>> setValueSafely(setting: ConfigSetting<T, V>, value : SerializableType<*>) {
    when (value) {
        is NumberType<*> -> {
            if(setting.defaultValueAsSerialized is NumberType<*>) {
                setting.setFromSerializedValue(value as V)
            }
        }
        is BooleanType -> {
            if(setting.defaultValueAsSerialized is BooleanType) {
                setting.setFromSerializedValue(value as V)
            }
        }
        is EnumType -> {
            if(setting.defaultValueAsSerialized is EnumType<*>) {
                setting.setFromSerializedValue(value as V)
            }
        }
        is ListType<*> -> {
            if(setting.defaultValueAsSerialized is ListType<*>) {
                setting.setFromSerializedValue(value as V)
            }
        }
        is StringType -> {
            if(setting.defaultValueAsSerialized is StringType) {
                setting.setFromSerializedValue(value as V)
            }
        }
    }
}