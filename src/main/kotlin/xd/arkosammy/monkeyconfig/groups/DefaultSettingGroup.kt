package xd.arkosammy.monkeyconfig.groups

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

open class DefaultSettingGroup @JvmOverloads constructor(
    name: String,
    comment: String? = null,
    override val configSettings: List<ConfigSetting<*, *>>,
    loadBeforeSave: Boolean = false,
    registerSettingsAsCommands: Boolean = true) : AbstractSettingGroup(name, comment, loadBeforeSave, registerSettingsAsCommands)