package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class BooleanSetting(name: String, comment: String?, value: Boolean) : ConfigSetting<Boolean>(name, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: Boolean) : ConfigSetting.Builder<Boolean, BooleanSetting>(id, defaultValue) {

        override fun build(): BooleanSetting {
            return BooleanSetting(this.id.settingName, this.comment, this.defaultValue)

        }

    }
}