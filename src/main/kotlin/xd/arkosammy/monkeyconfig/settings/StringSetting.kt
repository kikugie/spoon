package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class StringSetting(name: String, comment: String?, value: String) : ConfigSetting<String>(name, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: String) : ConfigSetting.Builder<String, StringSetting>(id, defaultValue) {

        override fun build(): StringSetting {
            return StringSetting(this.id.settingName, this.comment, this.defaultValue)
        }

    }
}