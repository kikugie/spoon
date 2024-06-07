package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class StringListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<String>, value: MutableList<String> = defaultValue) : ListSetting<String>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<String>) : ListSetting.Builder<String>(id, defaultValue) {

        override fun build(): StringListSetting {
            return StringListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}