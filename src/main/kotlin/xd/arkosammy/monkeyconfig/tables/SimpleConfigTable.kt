package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting

/**
 * A simple implementation of [ConfigTable] that stores its settings in a list.
 * Implementors of [ConfigTable] should use this class as a base class or as a reference for their own implementations.
 * This class uses a backing [List] to store its settings and allows for late addition of the config settings, and exposes a public property to access them. The backing field is updated with the settings provided by [addConfigSettings], whose parameter is copied to another immutable [List] to prevent further modification, and is then assigned to the backing field.
 */
class SimpleConfigTable @JvmOverloads constructor(override val name: String, override val comment: String? = null, override val loadBeforeSave: Boolean = false) : ConfigTable {

    override val configSettings: List<ConfigSetting<*>>
        get() = this._configSettings
    private var _configSettings: List<ConfigSetting<*>> = emptyList()

    override var isRegistered: Boolean = false

    override fun setAsRegistered() {
        this.isRegistered = true
    }

    override fun addConfigSettings(settings: List<ConfigSetting<*>>) : Boolean {
        if(this.isRegistered) {
            MonkeyConfig.LOGGER.warn("Attempted to add settings to table $name after it was registered!")
            return false
        }
        this._configSettings = settings.toList()
        return true
    }

    override fun toString(): String {
        return name
    }

}