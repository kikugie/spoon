package xd.arkosammy.monkeyconfig.tables

abstract class AbstractConfigTable @JvmOverloads constructor(
    override val name: String,
    override val comment: String? = null,
    override val loadBeforeSave: Boolean = false,
    override val registerSettingsAsCommands: Boolean) : ConfigTable {

    override var isRegistered: Boolean = false

    override fun setAsRegistered() {
        this.isRegistered = true
    }

    override fun toString(): String {
        return "${this::class.simpleName}{name=${this.name}, comment=${this.comment ?: "null"}, settingAmount=${this.configSettings.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave, registerSettingsAsCommands=$registerSettingsAsCommands}"
    }

}