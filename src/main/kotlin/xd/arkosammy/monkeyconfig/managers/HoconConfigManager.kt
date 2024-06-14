package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.hocon.HoconFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup

open class HoconConfigManager : AbstractConfigManager {

    constructor(
        configName: String,
        settingGroups: List<SettingGroup>) : super(configName, settingGroups, HoconFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.conf"))

    constructor(
        configName: String,
        settingGroups: List<MutableSettingGroup>? = null,
        settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, settingGroups, settingBuilders, HoconFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.conf"))

}