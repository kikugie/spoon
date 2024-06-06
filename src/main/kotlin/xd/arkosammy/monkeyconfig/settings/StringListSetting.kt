package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

// TODO: Make this an abstract generic class to support lists of any of the primitive data types
open class StringListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<String>, value: MutableList<String> = defaultValue) : ConfigSetting<MutableList<String>>(settingIdentifier, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<String>) : ConfigSetting.Builder<MutableList<String>, StringListSetting>(id, defaultValue) {

        override fun build(): StringListSetting {
            return StringListSetting(this.id, this.comment, this.defaultValue)
        }

    }
}