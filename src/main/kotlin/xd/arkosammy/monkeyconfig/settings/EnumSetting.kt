package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Test this!!
abstract class EnumSetting<E : Enum<E>> @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: E,
    value: E = defaultValue) : ConfigSetting<E, EnumType<E>>(settingIdentifier, comment, value) {

    val enumClass: Class<E> = defaultValue.declaringJavaClass

    abstract class Builder<E : Enum<E>> @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: E) : ConfigSetting.Builder<E, EnumType<E>, EnumSetting<E>>(id, comment, defaultValue) {

        abstract override fun build(): EnumSetting<E>

    }

}