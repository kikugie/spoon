package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class IntListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Int>, value: MutableList<Int> = defaultValue) : ListSetting<Int>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Int>) : ListSetting.Builder<Int>(id, defaultValue) {

        override fun build(): IntListSetting {
            return IntListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}