package xd.arkosammy.monkeyconfig.groups

/**
 * Base implementation of [SettingGroup] that implements basic logic for updating the [isRegistered] property.
 */
abstract class AbstractSettingGroup @JvmOverloads constructor(
    override val name: String,
    override val comment: String? = null,
    override val loadBeforeSave: Boolean = false,
    override val registerSettingsAsCommands: Boolean) : SettingGroup {

    protected var _isRegistered: Boolean = false

    override val isRegistered: Boolean
        get() = this._isRegistered

    override fun setAsRegistered() {
        this._isRegistered = true
    }

    override fun toString(): String =
        "${this::class.simpleName}{name=${this.name}, comment=${this.comment ?: "null"}, settingAmount=${this.configSettings.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave, registerSettingsAsCommands=$registerSettingsAsCommands}"

}