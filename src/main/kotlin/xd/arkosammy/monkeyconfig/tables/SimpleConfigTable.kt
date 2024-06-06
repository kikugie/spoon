package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

open class SimpleConfigTable(override val configSettings: List<ConfigSetting<*>>, name: String, comment: String? = null, loadBeforeSave: Boolean = false) : AbstractConfigTable(name, comment, loadBeforeSave)