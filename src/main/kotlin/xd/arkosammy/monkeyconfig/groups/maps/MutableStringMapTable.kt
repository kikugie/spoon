package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingLocation

// TODO: Test this
open class MutableStringMapTable @JvmOverloads constructor(
    defaultEntries: MutableList<ConfigSetting<StringType, StringType>>,
    mapEntries: MutableList<ConfigSetting<StringType, StringType>> = defaultEntries,
    name: String, comment: String? = null) : StringMapSettingGroup(name, comment, defaultEntries, mapEntries), MutableMapSettingGroup<StringType> {

    override val configSettings: MutableList<ConfigSetting<StringType, StringType>>
        get() = this.mapEntries

    override fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>) {
        val newValue = StringType(setting.serializedValue.value.toString())
        val defaultValue = StringType(setting.serializedDefaultValue.value.toString())
        val newSetting: ConfigSetting<StringType, StringType> = object : ConfigSetting<StringType, StringType>(SettingLocation(this.name, setting.settingLocation.settingName), defaultValue = defaultValue, value = newValue) {
            override val serializedValue: StringType
                get() = this.value
            override val serializedDefaultValue: StringType
                get() = this.defaultValue
            override fun setValueFromSerialized(serializedValue: StringType) {
                this.value = serializedValue
            }
        }
        this.mapEntries.add(newSetting)
    }

    override fun toImmutable(): AbstractMapSettingGroup<StringType> {
        return StringMapSettingGroup(this.name, this.comment, this.defaultTableEntries.toList(), this.mapEntries.toList())
    }

}