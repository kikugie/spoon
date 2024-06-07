package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Test this!!
abstract class EnumSetting<T : Enum<T>>(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: T, value: T = defaultValue) : ConfigSetting<T>(settingIdentifier, comment, value) {

    val enumClass: Class<T> = defaultValue.declaringJavaClass

    abstract class Builder<T : Enum<T>>(id: SettingIdentifier, defaultValue: T) : ConfigSetting.Builder<T, EnumSetting<T>>(id, defaultValue) {

        abstract override fun build(): EnumSetting<T>

    }

}