package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

sealed class ListSetting<T>(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<T>, value: MutableList<T> = defaultValue) : ConfigSetting<MutableList<T>>(settingIdentifier, comment, value) {

    abstract class Builder<T>(id: SettingIdentifier, defaultValue: MutableList<T>) : ConfigSetting.Builder<MutableList<T>, ListSetting<T>>(id, defaultValue) {

        abstract override fun build(): ListSetting<T>

    }

}