package xd.arkosammy.monkeyconfig.tables.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Test this
open class MutableStringMapTable @JvmOverloads constructor(
    defaultEntries: MutableList<ConfigSetting<StringType, StringType>>,
    tableEntries: MutableList<ConfigSetting<StringType, StringType>> = defaultEntries,
    name: String, comment: String? = null) : StringMapTable(defaultEntries, tableEntries, name, comment), MutableMapConfigTable<StringType> {

    override val configSettings: MutableList<ConfigSetting<StringType, StringType>>
        get() = this.tableEntries

    override fun <T, S : SerializableType<*>> addConfigSetting(setting: ConfigSetting<T, S>) {
        val newValue = StringType(setting.serializedValue.value.toString())
        val defaultValue = StringType(setting.serializedDefaultValue.value.toString())
        val newSetting: ConfigSetting<StringType, StringType> = object : ConfigSetting<StringType, StringType>(SettingIdentifier(this.name, setting.settingIdentifier.settingName), defaultValue = defaultValue, value = newValue) {
            override val serializedValue: StringType
                get() = this.value
            override val serializedDefaultValue: StringType
                get() = this.defaultValue
            override fun setValueFromSerialized(serializedValue: StringType) {
                this.value = serializedValue
            }
        }
        this.tableEntries.add(newSetting)
    }

    override fun toImmutable(): AbstractMapConfigTable<StringType> {
        return StringMapTable(this.defaultTableEntries.toList(), this.tableEntries.toList(), this.name, this.comment)
    }

}