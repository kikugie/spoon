package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting

class SimpleConfigTable(override val name: String, override val comment: String? = null, override val loadBeforeSave: Boolean = false) : ConfigTable {

    override val configSettings: MutableList<ConfigSetting<*>> = ArrayList()
    override var isRegistered: Boolean = false

    override fun setAsRegistered() {
        this.isRegistered = true
    }

}