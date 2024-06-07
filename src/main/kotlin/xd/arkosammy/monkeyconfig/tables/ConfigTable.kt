package xd.arkosammy.monkeyconfig.tables

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.EnumSetting

/**
 * Represents a container of config settings useful for grouping related config settings under a common namespace. They can be registered with a config manager to be loaded from and saved to a config file.
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
     * An immutable view of the config settings in this config table. This property should always return an immutable view of the settings in this table.
     */
    val configSettings: List<ConfigSetting<*>>

    /**
     * Whether this table has been registered with a config manager. This method should be set using {@code setAsRegistered()} after initializing the table and adding it to the config manager to prevent further settings from being added.
     */
    var isRegistered: Boolean

    /**
     * Whether this table should be loaded before being saved to file. This is useful for tables whose values should be updated in memory before being written to the config file again
     */
    val loadBeforeSave : Boolean

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
        for(setting: ConfigSetting<*> in this.configSettings) {
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
        for(setting: ConfigSetting<*> in this.configSettings) {
            val settingAddress: String = "${this.name}.${setting.settingIdentifier}"
            fileConfig.set<Any>(settingAddress, setting.value)
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
     * @param fileConfig The {@code CommentedFileConfig} instance from which to load the values of the settings from
     */
    fun loadValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<*> in this.configSettings) {
            val settingAddress: String = "${this.name}.${setting.settingIdentifier}"
            val value: Any? = if(setting is EnumSetting<*>) fileConfig.getEnum(settingAddress, setting.enumClass) ?: setting.defaultValue else fileConfig.getOrElse(settingAddress, setting.defaultValue)
            setValueSafely(setting, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> setValueSafely(setting: ConfigSetting<T>, value: Any?) {
        var safeValue: Any? = value
        setting.defaultValue.let { defaultValue ->
            when (defaultValue::class) {
                Integer::class -> if (value is Number) safeValue = value.toInt()
                Double::class -> if (value is Number) safeValue = value.toDouble()
            }
            if (defaultValue::class.isInstance(safeValue)) {
                setting.value = safeValue as T
            } else {
                MonkeyConfig.LOGGER.error("Failed to load value for setting ${setting.settingIdentifier} in table ${this.name}. Expected ${defaultValue::class.simpleName}, got ${safeValue?.javaClass?.simpleName}!. Using default value instead")
            }
        }
    }

    /**
     * Checks whether this table contains a setting with the name [settingName].
     * @return whether this table contains a setting with the name [settingName]
     */
    fun containsSettingName(settingName: String) : Boolean {
        for(setting: ConfigSetting<*> in this.configSettings) {
            if(setting.settingIdentifier.settingName == settingName) {
                return true
            }
        }
        return false
    }


}