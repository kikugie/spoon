package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class NumberListSetting<T : Number> @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: List<T>,
    value: List<T> = defaultValue) : ListSetting<T, NumberType<T>>(settingIdentifier, comment, defaultValue, value) {

    override val serializedValue: ListType<NumberType<T>>
        get() = ListType(this.value.toList().map { e -> NumberType(e) })

    override val serializedDefaultValue: ListType<NumberType<T>>
        get() = ListType(this.defaultValue.toList().map { e -> NumberType(e) })

    override fun setValueFromSerialized(serializedValue: ListType<NumberType<T>>) {
        this.value = serializedValue.value.toList().map { e -> e.value }
    }

    class Builder<T : Number> @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: List<T>) : ListSetting.Builder<T, NumberType<T>>(id, comment, defaultValue) {

        override fun build(): NumberListSetting<T> {
            return NumberListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}