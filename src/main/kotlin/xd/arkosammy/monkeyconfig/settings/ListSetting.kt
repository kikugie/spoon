package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

abstract class ListSetting<T, V : SerializableType<*>>(
    settingIdentifier: SettingIdentifier, comment: String? = null,
    defaultValue: List<T>,
    value: List<T> = defaultValue) : ConfigSetting<List<T>, ListType<V>>(settingIdentifier, comment, value) {

    abstract class Builder<T, V : SerializableType<*>>(id: SettingIdentifier, comment: String? = null, defaultValue: List<T>) : ConfigSetting.Builder<List<T>, ListType<V>, ListSetting<T, V>>(id, comment, defaultValue) {

        abstract override fun build(): ListSetting<T, V>

    }

}