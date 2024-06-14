package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingLocation

// TODO: Test this!!
open class EnumSetting<E : Enum<E>> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : ConfigSetting<E, EnumType<E>>(settingLocation, comment, value) {

    val enumClass: Class<E> = defaultValue.declaringJavaClass

    override val serializedValue: EnumType<E>
        get() = EnumType(this.value)

    override val serializedDefaultValue: EnumType<E>
        get() = EnumType(this.defaultValue)

    override fun setValueFromSerialized(serializedValue: EnumType<E>) {
        this.value = serializedValue.value
    }
    open class Builder<E : Enum<E>> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: E) : ConfigSetting.Builder< EnumSetting<E>, E, EnumType<E>>(settingLocation, comment, defaultValue) {

        override fun build(): EnumSetting<E> {
            return EnumSetting(this.settingLocation, this.comment, this.defaultValue)
        }

    }

}