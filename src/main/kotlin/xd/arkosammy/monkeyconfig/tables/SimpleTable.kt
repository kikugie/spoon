package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

open class SimpleTable @JvmOverloads constructor(
    override val configSettings: List<ConfigSetting<*, *>>,
    name: String, comment: String? = null,
    loadBeforeSave: Boolean = false,
    registerSettingsAsCommands: Boolean = true) : AbstractConfigTable(name, comment, loadBeforeSave, registerSettingsAsCommands)