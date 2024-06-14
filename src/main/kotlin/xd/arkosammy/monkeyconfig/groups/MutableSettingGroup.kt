package xd.arkosammy.monkeyconfig.groups

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation

/**
 * A [SettingGroup] that supports adding and removing [ConfigSetting] instances from it.
 */
interface MutableSettingGroup : SettingGroup {

    /**
     * Adds a [ConfigSetting] to this [SettingGroup].
     */
    fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>)

    /**
     * Removes a [ConfigSetting] from this [SettingGroup] by its [SettingLocation].
     *
     * @return `true` if the setting was successfully removed, `false` otherwise.
     */
    fun removeConfigSetting(settingLocation: SettingLocation) : Boolean = false

    /**
     * Returns an instance of [SettingGroup] that is immutable and with the same values as this [SettingGroup], meaning that it must not be an instance of [MutableSettingGroup].
     */
    fun toImmutable() : SettingGroup

}