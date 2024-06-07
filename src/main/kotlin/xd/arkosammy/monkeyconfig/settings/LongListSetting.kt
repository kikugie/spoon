package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class LongListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Long>, value: MutableList<Long> = defaultValue) : ListSetting<Long>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Long>) : ListSetting.Builder<Long>(id, defaultValue) {

        override fun build(): LongListSetting {
            return LongListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}