package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class CharSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: Char, value: Char = defaultValue) : ConfigSetting<Char>(settingIdentifier, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: Char) : ConfigSetting.Builder<Char, CharSetting>(id, defaultValue) {

        override fun build(): CharSetting {
            return CharSetting(this.id, this.comment, this.defaultValue)
        }

    }

}