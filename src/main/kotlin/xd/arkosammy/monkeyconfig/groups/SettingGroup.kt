package xd.arkosammy.monkeyconfig.groups

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.EnumSetting
import xd.arkosammy.monkeyconfig.types.*
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.groups.maps.MapSettingGroup

/**
 * Represents a container of [ConfigSetting]s useful for grouping related config settings under a common namespace
 * and for serializing using a [ConfigManager].
 * They can be registered with a [ConfigManager] to be loaded from and saved to a config file.
 */
interface SettingGroup {

    /**
     * The name of this [SettingGroup]
     */
    val name: String

    /**
     * The comment for this [SettingGroup] to be written to the config file, if any.
     */
    val comment: String?

    /**
     * The list of [ConfigSetting] instances stored in this [SettingGroup]. This list should ideally be immutable.
     */
    val configSettings: List<ConfigSetting<*, *>>

    /**
     * Whether this table has been registered with a config manager.
     * This value should be set using [SettingGroup.setAsRegistered] after initializing the table
     * and adding it to a [ConfigManager] to prevent further settings from being added.
     */
    val isRegistered: Boolean

    /**
     * Whether this table should be loaded before being saved to file. This is useful for tables whose values should be updated in memory before being written to the config file again
     */
    val loadBeforeSave : Boolean

    /**
     * Whether the [ConfigSetting] instances should be used
     * to register commands for editing the values of those settings.
     * Should be `false` if the [SettingGroup]'s [ConfigSetting] instances can change during runtime,
     * should as with [MapSettingGroup]
     */
    val registerSettingsAsCommands: Boolean

    /**
     * Sets this [SettingGroup] as registered.
     * This method should be called after the [SettingGroup] has been added to a [ConfigManager].
     * Once a [SettingGroup] has been registered, it should not be unregistered again.
     */
    fun setAsRegistered()

    /**
     * Invoked by a [ConfigManager] after and when this [SettingGroup] has been registered.
     */
    fun onRegistered() {}

    /**
     * Invoked by a [ConfigManager] after and when this [SettingGroup]'s values are loaded from a config file.
     */
    fun onLoaded() {}

    /**
     * Invoked by a [ConfigManager] after and when this [SettingGroup]'s values are saved to a config file.
     */
    fun onSavedToFile() {}

    /**
     * Sets the values of the [ConfigSetting]s in this [SettingGroup] to their default values,
     * then writes them to [fileConfig].
     *
     * @param fileConfig The [FileConfig] instance to which to write the values of this [SettingGroup] to.
     */
    fun setDefaultValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            setting.resetValue()
        }
        this.setValues(fileConfig)
    }

    /**
     * Writes the current values of the settings in this [SettingGroup] to [fileConfig].
     *
     * @param fileConfig The [FileConfig] instance to which to write the values of this [SettingGroup] to.
     */
    fun setValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            val settingLocation = "${setting.settingLocation.groupName}.${setting.settingLocation.settingName}"
            val valueAsSerialized: SerializableType<*> = setting.serializedValue
            fileConfig.set<Any>(settingLocation, if(valueAsSerialized is ListType<*>) valueAsSerialized.fullyDeserializedList else valueAsSerialized.value)
            setting.comment?.let { comment ->
                if(fileConfig is CommentedFileConfig) fileConfig.setComment(settingLocation, comment)
            }
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
        val configSection: Config = fileConfig.get(this.name)
        configSection.entrySet().removeIf { entry -> !this.containsSettingName(entry.key)}
    }

    /**
     * Updates the values of the [ConfigSetting]s in this [SettingGroup] by reading them from [fileConfig].
     *
     * @param fileConfig The [FileConfig] instance from which to read the values from.
     */
    fun loadValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*, *> in this.configSettings) {
            val settingLocation = "${setting.settingLocation.groupName}.${setting.settingLocation.settingName}"
            val defaultDeserializedValue: Any = setting.serializedDefaultValue.value
            val rawValue: Any = if (setting is EnumSetting<*>) {
                fileConfig.getEnum(settingLocation, setting.enumClass) ?: defaultDeserializedValue
            } else {
                fileConfig.getOrElse(settingLocation, defaultDeserializedValue) ?: defaultDeserializedValue
            }
            val deserializedValue: SerializableType<*> = toSerializedType(rawValue)
            setValueSafely(setting, deserializedValue)
        }
    }

    /**
     * Checks whether this [SettingGroup] contains [ConfigSetting] with a matching [settingName].
     *
     * @param [settingName] The name of the [ConfigSetting] to look for.
     * @return whether this [SettingGroup] contains a [ConfigSetting] whose name matches [settingName].
     */
    fun containsSettingName(settingName: String) : Boolean =
        this.configSettings.any { setting -> setting.settingLocation.settingName == settingName }

}

/**
 * Attempts to safely assign a [SerializableType] as the value of a [ConfigSetting] by checking the type of [value], and calling [ConfigSetting.setValueFromSerialized] accordingly.
 *
 * @param setting The setting to assign a [SerializableType] value to
 * @param value The [SerializableType] to assign as the value of the [setting]
 */
@Suppress("UNCHECKED_CAST")
fun <T, V : SerializableType<*>> setValueSafely(setting: ConfigSetting<T, V>, value : SerializableType<*>) {
    when (value) {
        is NumberType<*> -> {
            if(setting.serializedDefaultValue is NumberType<*>) {
                setting.setValueFromSerialized(value as V)
            }
        }
        is BooleanType -> {
            if(setting.serializedDefaultValue is BooleanType) {
                setting.setValueFromSerialized(value as V)
            }
        }
        is EnumType -> {
            if(setting.serializedDefaultValue is EnumType<*>) {
                setting.setValueFromSerialized(value as V)
            }
        }
        is ListType<*> -> {
            if(setting.serializedDefaultValue is ListType<*>) {
                setting.setValueFromSerialized(value as V)
            }
        }
        is StringType -> {
            if(setting.serializedDefaultValue is StringType) {
                setting.setValueFromSerialized(value as V)
            }
        }
    }
}