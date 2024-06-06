package xd.arkosammy.monkeyconfig.tables

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

interface MutableConfigTable : ConfigTable {

    /**
     * Adds a [ConfigSetting] to this [ConfigTable].
     */
    fun addConfigSetting(settings: ConfigSetting<*>)

    /**
     * Removes a [ConfigSetting] from this [ConfigTable] by its [SettingIdentifier].
     *
     * @return `true` if the setting was removed, `false` otherwise.
     */
    fun removeConfigSetting(settingId: SettingIdentifier) : Boolean = false

    /**
     * Returns an instance of [ConfigTable] that is immutable and with the same values as this [ConfigTable], meaning that it must not be an instance of [MutableConfigTable].
     */
    fun toImmutable() : ConfigTable

}