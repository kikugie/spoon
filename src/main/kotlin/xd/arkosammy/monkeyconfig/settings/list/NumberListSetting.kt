package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class NumberListSetting<T : Number>(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: List<T>,
    value: List<T> = defaultValue) : ListSetting<T, NumberType<T>>(settingIdentifier, comment, defaultValue, value) {

    override val valueAsSerialized: ListType<NumberType<T>>
        get() = ListType(this.value.toList().map { e -> NumberType(e) })

    override val defaultValueAsSerialized: ListType<NumberType<T>>
        get() = ListType(this.defaultValue.toList().map { e -> NumberType(e) })

    override fun setFromSerializedValue(serializedValue: ListType<NumberType<T>>) {
        this.value = serializedValue.value.toList().map { e -> e.value }
    }

    class Builder<T : Number>(id: SettingIdentifier, comment: String? = null, defaultValue: List<T>) : ListSetting.Builder<T, NumberType<T>>(id, comment, defaultValue) {

        override fun build(): NumberListSetting<T> {
            return NumberListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}