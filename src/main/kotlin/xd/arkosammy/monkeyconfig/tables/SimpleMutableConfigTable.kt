package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

open class SimpleMutableConfigTable(override val configSettings: MutableList<ConfigSetting<*>>, name: String, comment: String? = null, loadBeforeSave: Boolean = false) : AbstractConfigTable(name, comment, loadBeforeSave), MutableConfigTable  {

    override fun addConfigSetting(settings: ConfigSetting<*>) {
        this.configSettings.add(settings)
    }

    override fun toImmutable(): ConfigTable {
        return SimpleConfigTable(configSettings.toList(), name, comment, loadBeforeSave)
    }
}