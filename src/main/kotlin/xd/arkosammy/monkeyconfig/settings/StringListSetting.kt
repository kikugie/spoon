package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class StringListSetting(name: String, comment: String?, value: MutableList<String>) : ConfigSetting<MutableList<String>>(name, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<String>) : ConfigSetting.Builder<MutableList<String>, StringListSetting>(id, defaultValue) {

        override fun build(): StringListSetting {
            return StringListSetting(this.id.settingName, this.comment, this.defaultValue)
        }

    }
}