package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class StringListSetting(configManager: ConfigManager, settingIdentifier: SettingIdentifier, comment: String?, value: MutableList<String>) : ConfigSetting<MutableList<String>>(configManager, settingIdentifier, comment, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<String>) : ConfigSetting.Builder<MutableList<String>, StringListSetting>(id, defaultValue) {

        override fun build(configManager: ConfigManager): StringListSetting {
            return StringListSetting(configManager, this.id, this.comment, this.defaultValue)
        }

    }
}