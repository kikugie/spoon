package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.yaml.YamlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup

// Internal class as it's not ready to be used yet
internal class YamlConfigManager : AbstractConfigManager {

    constructor(
        configName: String,
        settingGroups: List<SettingGroup>) : super(configName, settingGroups, YamlFormat.defaultInstance(), FabricLoader.getInstance().configDir.resolve("$configName.yaml"))

    constructor(
        configName: String,
        settingGroups: List<MutableSettingGroup>? = null,
        settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, settingGroups, settingBuilders, YamlFormat.defaultInstance(), FabricLoader.getInstance().configDir.resolve("$configName.yaml"))

}