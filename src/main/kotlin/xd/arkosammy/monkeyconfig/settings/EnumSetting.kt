package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.EnumType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Test this!!
abstract class EnumSetting<T : Enum<T>>(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: T,
    value: T = defaultValue) : ConfigSetting<T, EnumType<T>>(settingIdentifier, comment, value) {

    val enumClass: Class<T> = defaultValue.declaringJavaClass

    abstract class Builder<T : Enum<T>>(id: SettingIdentifier, comment: String? = null, defaultValue: T) : ConfigSetting.Builder<T, EnumType<T>, EnumSetting<T>>(id, comment, defaultValue) {

        abstract override fun build(): EnumSetting<T>

    }

}