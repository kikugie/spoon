package xd.arkosammy.monkeyconfig.util

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup

/**
 * A data class used to associate a [ConfigSetting] to its [SettingGroup].
 */
@JvmRecord
data class SettingLocation(val groupName: String, val settingName: String) {

    override fun toString(): String {
        return "$groupName.$settingName"
    }

}