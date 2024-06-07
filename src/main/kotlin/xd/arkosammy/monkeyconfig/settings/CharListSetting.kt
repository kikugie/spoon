package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class CharListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Char>, value: MutableList<Char> = defaultValue) : ListSetting<Char>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Char>) : ListSetting.Builder<Char>(id, defaultValue) {

        override fun build(): CharListSetting {
            return CharListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}