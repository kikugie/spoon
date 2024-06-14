package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.json.JsonFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup

open class JsonConfigManager : AbstractConfigManager {

    constructor(
        configName: String,
        settingGroups: List<SettingGroup>) : super(configName, settingGroups, JsonFormat.fancyInstance(), FabricLoader.getInstance().configDir.resolve("$configName.json"))

    constructor(
        configName: String,
        settingGroups: List<MutableSettingGroup>? = null,
        settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, settingGroups, settingBuilders, JsonFormat.fancyInstance(), FabricLoader.getInstance().configDir.resolve("$configName.json"))

}