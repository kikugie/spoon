package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

abstract class ListSetting<E, S : SerializableType<*>>(
    settingIdentifier: SettingIdentifier, comment: String? = null,
    defaultValue: List<E>,
    value: List<E> = defaultValue) : ConfigSetting<List<E>, ListType<S>>(settingIdentifier, comment, value) {

    abstract class Builder<E, S : SerializableType<*>>(id: SettingIdentifier, comment: String? = null, defaultValue: List<E>) : ConfigSetting.Builder<List<E>, ListType<S>, ListSetting<E, S>>(id, comment, defaultValue) {

        abstract override fun build(): ListSetting<E, S>

    }

}