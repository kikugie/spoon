package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class StringListSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: List<String>,
    value: List<String> = defaultValue) : ListSetting<String, StringType>(settingLocation, comment, defaultValue, value) {

    override val valueToSerializedConverter: (List<String>) -> ListType<StringType>
        get() = { stringList -> ListType(stringList.toList().map { e -> StringType(e) }) }

    override val serializedToValueConverter: (ListType<StringType>) -> List<String>
        get() = { serializedStringList -> serializedStringList.rawValue.toList().map { e -> e.rawValue } }

    open class Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: List<String>) : ListSetting.Builder<String, StringType>(settingLocation, comment, defaultValue) {

        override fun build(): StringListSetting = StringListSetting(this.settingLocation, this.comment, this.defaultValue)

    }

}