package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.StringSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class StringMapSettingGroup @JvmOverloads constructor(
    name: String,
    comment: String? = null,
    defaultEntries: Map<String, String>,
    mapEntries: Map<String, String> = defaultEntries) : AbstractMapSettingGroup<String, StringType>(name, comment, defaultEntries, mapEntries) {

    constructor(name: String, comment: String?, defaultEntries: List<ConfigSetting<String, StringType>>, entries: List<ConfigSetting<String, StringType>> = defaultEntries) : this(name, comment,
        defaultEntries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }, entries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }
    )

    override fun getMapEntryFromSerialized(settingLocation: SettingLocation, serializedEntry: SerializableType<*>): ConfigSetting<String, StringType>? {
        if (serializedEntry !is StringType) {
            return null
        }
        return StringSetting(settingLocation, defaultValue = serializedEntry.rawValue, value = serializedEntry.rawValue)
    }

    override fun getMapEntryFromValue(settingLocation: SettingLocation, defaultValue: String, value: String): ConfigSetting<String, StringType> =
        StringSetting(settingLocation, defaultValue = defaultValue, value = value)

}