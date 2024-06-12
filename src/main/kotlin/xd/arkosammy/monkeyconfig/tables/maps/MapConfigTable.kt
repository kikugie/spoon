package xd.arkosammy.monkeyconfig.tables.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

interface MapConfigTable<V : SerializableType<*>> : ConfigTable {

    /**
     * Returns the [SerializableType] instance associated to the [key] parameter.
     *
     * @param key The [SettingIdentifier] name of the [ConfigSetting]
     * @return the [SerializableType] instance if found, or `null` otherwise
     */
    fun get(key: String) : V?

    override val loadBeforeSave : Boolean
        get() = true

    override val registerSettingsAsCommands: Boolean
        get() = false

}