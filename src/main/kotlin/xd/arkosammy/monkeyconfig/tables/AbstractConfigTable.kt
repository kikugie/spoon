package xd.arkosammy.monkeyconfig.tables

abstract class AbstractConfigTable @JvmOverloads constructor(override val name: String, override val comment: String? = null, override val loadBeforeSave: Boolean = false) : ConfigTable {

    override var isRegistered: Boolean = false

    override fun setAsRegistered() {
        this.isRegistered = true
    }

    override fun toString(): String {
        return name
    }

}