package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class BooleanListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Boolean>, value: MutableList<Boolean> = defaultValue) : ListSetting<Boolean>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Boolean>) : ListSetting.Builder<Boolean>(id, defaultValue) {

        override fun build(): BooleanListSetting {
            return BooleanListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}