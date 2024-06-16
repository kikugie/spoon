package xd.arkosammy.monkeyconfig.groups

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType

open class DefaultMutableSettingGroup @JvmOverloads constructor(
    name: String,
    comment: String? = null,
    override val configSettings: MutableList<ConfigSetting<*, *>> = mutableListOf(),
    loadBeforeSave: Boolean = false,
    registerSettingsAsCommands: Boolean = true) : DefaultSettingGroup(name, comment, configSettings, loadBeforeSave, registerSettingsAsCommands), MutableSettingGroup {

    override fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>) {
        this.configSettings.add(setting)
    }

    override fun toImmutable(): SettingGroup {
        return DefaultSettingGroup(name, comment, configSettings.toList(), loadBeforeSave)
    }
}