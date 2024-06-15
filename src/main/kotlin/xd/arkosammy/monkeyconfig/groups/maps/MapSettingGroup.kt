package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation

/**
 * This implementation
 * of [SettingGroup] associates each of the [ConfigSetting] instances to its [SettingLocation] name.
 * All [ConfigSetting] instances stored in this [SettingGroup] are written to
 * and read from in bulk from a configuration file,
 * thus, it shouldn't be used to create [CommandControllableSetting] instances,
 * as the entries of this table can change during runtime by editing the config file.
 *
 * @param [V] The type of the values that this [MapSettingGroup] holds in memory.
 * @param [S] The [SerializableType] that will be used to write and read this [MapSettingGroup]'s values from the configuration file.
 */
interface MapSettingGroup<V, S : SerializableType<*>> : SettingGroup {


    /**
     * Returns the [SerializableType] instance associated to the [key] parameter.
     *
     * @param key The [SettingLocation] name of the [ConfigSetting]
     * @return the [SerializableType] instance if found, or `null` otherwise
     */
    fun get(key: String) : V?

    override val loadBeforeSave : Boolean
        get() = true

    override val registerSettingsAsCommands: Boolean
        get() = false


}