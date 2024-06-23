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
     * Transforms this [MutableSettingGroup] into an immutable [SettingGroup].
     *
     * The resulting [SettingGroup] will not be an instance of [MutableSettingGroup] and will contain the same [ConfigSetting]s as this [MutableSettingGroup]. If the [configSettings] argument is not `null`, the resulting [SettingGroup] will contain the [ConfigSetting]s passed to this method instead.
     *
     * @param configSettings An optional [List] of [ConfigSetting]s to be included in the returned [SettingGroup]. If `null`, the [ConfigSetting]s of this [MutableSettingGroup] will be used.
     * @return An immutable [SettingGroup] with the same or specified [ConfigSetting]s.
     */
    fun toImmutable(configSettings: List<ConfigSetting<*, *>>? = null) : SettingGroup

}