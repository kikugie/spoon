package xd.arkosammy.monkeyconfig.tables

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting

interface ConfigTable {

    val name: String

    val comment: String?

    val configSettings: MutableList<ConfigSetting<*>>

    var isRegistered: Boolean

    val loadBeforeSave : Boolean

    fun setAsRegistered()

    fun onRegistered() {}

    fun onLoaded() {}

    fun onSavedToFile() {}

    fun addConfigSetting(setting: ConfigSetting<*>) {
        if (this.isRegistered) {
            return
        }
        this.configSettings.add(setting)
    }

    fun setDefaultValues(fileConfig: CommentedFileConfig) {
        for(setting: ConfigSetting<*> in this.configSettings) {
            setting.resetValue()
        }
        this.setValues(fileConfig)
    }

    fun setValues(fileConfig: CommentedFileConfig) {

        for(setting: ConfigSetting<*> in this.configSettings) {
            val settingAddress: String = "${this.name}.${setting.name}"
            fileConfig.set<Any>(settingAddress, setting.value)
            setting.comment?.let { comment -> fileConfig.setComment(settingAddress, comment) }
        }
        this.comment?.let { comment -> fileConfig.setComment(this.name, comment) }
        val tableConfig: CommentedConfig = fileConfig.get(this.name)
        tableConfig.entrySet().removeIf { entry -> !this.containsSettingName(entry.key)}

    }

    fun loadValues(fileConfig: CommentedFileConfig) {

        for(setting: ConfigSetting<*> in this.configSettings) {
            val settingAddress: String = "${this.name}.${setting.name}"
            val value: Any? = fileConfig.getOrElse(settingAddress, setting.defaultValue)
            setValueSafely(setting, value)
        }

    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> setValueSafely(setting: ConfigSetting<T>, value: Any?) {
        var safeValue: Any? = value
        setting.defaultValue?.let { defaultValue ->
            when (defaultValue::class) {
                Integer::class -> if (value is Number) safeValue = value.toInt()
                Double::class -> if (value is Number) safeValue = value.toDouble()
            }
            if (defaultValue::class.isInstance(safeValue)) {
                setting.value = safeValue as T
            } else {
                MonkeyConfig.LOGGER.error("Failed to load value for setting ${setting.name} in table ${this.name}. Expected ${defaultValue::class.simpleName}, got ${safeValue?.javaClass?.simpleName}!. Using default value instead")
            }
        }
    }

    fun containsSettingName(settingName: String) : Boolean {
        for(setting: ConfigSetting<*> in this.configSettings) {
            if(setting.name == settingName) {
                return true
            }
        }
        return false
    }


}