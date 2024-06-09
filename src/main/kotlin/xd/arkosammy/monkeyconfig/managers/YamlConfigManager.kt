package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.yaml.YamlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable

open class YamlConfigManager : AbstractConfigManager {

    constructor(configName: String, configTables: List<ConfigTable>) : super(configName, configTables, YamlFormat.defaultInstance(), FabricLoader.getInstance().configDir.resolve("$configName.yaml"))

    constructor(configName: String, configTables: List<MutableConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, configTables, settingBuilders, YamlFormat.defaultInstance(), FabricLoader.getInstance().configDir.resolve("$configName.yaml"))

}