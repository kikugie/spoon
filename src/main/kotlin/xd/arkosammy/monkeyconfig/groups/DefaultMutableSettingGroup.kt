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

    override fun toImmutable(configSettings: List<ConfigSetting<*, *>>?): SettingGroup =
        DefaultSettingGroup(this.name, this.comment, configSettings?.toList() ?: this.configSettings , this.loadBeforeSave)

}