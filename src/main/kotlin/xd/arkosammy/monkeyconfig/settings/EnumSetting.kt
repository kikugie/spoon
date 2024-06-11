package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Test this!!
open class EnumSetting<E : Enum<E>> @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : ConfigSetting<E, EnumType<E>>(settingIdentifier, comment, value) {

    val enumClass: Class<E> = defaultValue.declaringJavaClass

    override val serializedValue: EnumType<E>
        get() = EnumType(this.value)

    override val serializedDefaultValue: EnumType<E>
        get() = EnumType(this.defaultValue)

    override fun setValueFromSerialized(serializedValue: EnumType<E>) {
        this.value = serializedValue.value
    }
    open class Builder<E : Enum<E>> @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: E) : ConfigSetting.Builder<E, EnumType<E>, EnumSetting<E>>(id, comment, defaultValue) {

        override fun build(): EnumSetting<E> {
            return EnumSetting(this.id, this.comment, this.defaultValue)
        }

    }

}