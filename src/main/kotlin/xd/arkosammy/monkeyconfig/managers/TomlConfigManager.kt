package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.toml.TomlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup

open class TomlConfigManager : AbstractConfigManager {

    constructor(
        configName: String,
        settingGroups: List<SettingGroup>) : super(configName, settingGroups, TomlFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.toml"))

    constructor(
        configName: String,
        settingGroups: List<MutableSettingGroup>? = null,
        settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, settingGroups, settingBuilders, TomlFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.toml"))

}