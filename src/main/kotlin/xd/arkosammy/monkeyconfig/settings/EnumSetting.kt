package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class EnumSetting<E : Enum<E>> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : ConfigSetting<E, EnumType<E>>(settingLocation, comment, value) {

    val enumClass: Class<E> = defaultValue.declaringJavaClass

    override val valueToSerializedConverter: (E) -> EnumType<E>
        get() = { enumInstance -> EnumType(enumInstance) }

    override val serializedToValueConverter: (EnumType<E>) -> E
        get() = { enumType -> enumType.rawValue }

    open class Builder<E : Enum<E>> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: E) : ConfigSetting.Builder< EnumSetting<E>, E, EnumType<E>>(settingLocation, comment, defaultValue) {

        override fun build(): EnumSetting<E> = EnumSetting(this.settingLocation, this.comment, this.defaultValue)

    }

}