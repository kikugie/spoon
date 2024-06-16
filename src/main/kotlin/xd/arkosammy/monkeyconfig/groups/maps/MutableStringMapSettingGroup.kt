package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class MutableStringMapSettingGroup @JvmOverloads constructor(
    name: String,
    comment: String? = null,
    defaultEntries: Map<String, String> = mutableMapOf(),
    mapEntries: Map<String, String> = defaultEntries) : StringMapSettingGroup(name, comment, defaultEntries, mapEntries), MutableMapSettingGroup<String, StringType> {

    constructor(name: String, comment: String?, defaultEntries: List<ConfigSetting<String, StringType>>, entries: List<ConfigSetting<String, StringType>> = defaultEntries) : this(name, comment,
        defaultEntries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }, entries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }
    )
    override val configSettings: MutableList<ConfigSetting<String, StringType>>
        get() = this.mapEntries

    override fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>) {
        val newSetting: ConfigSetting<String, StringType> = this.getMapEntryFromValue(SettingLocation(this.name, setting.settingLocation.settingName), setting.defaultValue.toString(), setting.value.toString())
        this.mapEntries.add(newSetting)
    }

    override fun toImmutable(): AbstractMapSettingGroup<String, StringType> {
        return StringMapSettingGroup(this.name, this.comment, this.defaultTableEntries.toList(), this.mapEntries.toList())
    }

}