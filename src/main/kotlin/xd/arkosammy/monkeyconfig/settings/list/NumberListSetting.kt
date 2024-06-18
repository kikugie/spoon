package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class NumberListSetting<T : Number> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: List<T>,
    value: List<T> = defaultValue) : ListSetting<T, NumberType<T>>(settingLocation, comment, defaultValue, value) {

    override val valueToSerializedConverter: (List<T>) -> ListType<NumberType<T>>
        get() = { numberList -> ListType(numberList.toList().map { e -> NumberType(e) }) }

    override val serializedToValueConverter: (ListType<NumberType<T>>) -> List<T>
        get() = { serializedNumberList -> serializedNumberList.rawValue.toList().map { e -> e.rawValue } }

    open class Builder<T : Number> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: List<T>) : ListSetting.Builder<T, NumberType<T>>(settingLocation, comment, defaultValue) {

        override fun build(): NumberListSetting<T> = NumberListSetting(this.settingLocation, this.comment, this.defaultValue)

    }

}