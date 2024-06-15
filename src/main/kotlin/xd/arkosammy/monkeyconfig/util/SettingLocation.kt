package xd.arkosammy.monkeyconfig.util

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * A data class used to associate a [ConfigSetting] to its [SettingGroup] via their names.
 * This class is primarily used
 * to assign [ConfigSetting]s to [SettingGroup]s when passing [ConfigSetting.Builder] instances to a [ConfigManager] constructor,
 * to allow it to correctly populate [SettingGroup] with the associated [ConfigSetting]s.
 * It is also used to query [ConfigSetting]s from [SettingGroup]s and [ConfigManager]s.
 */
@JvmRecord
data class SettingLocation(val groupName: String, val settingName: String) {

    override fun toString(): String {
        return "$groupName.$settingName"
    }

}