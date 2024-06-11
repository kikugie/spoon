package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.toml.TomlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable

open class TomlConfigManager : AbstractConfigManager {

    constructor(configName: String, configTables: List<ConfigTable>) : super(configName, configTables, TomlFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.toml"))

    constructor(configName: String, configTables: List<MutableConfigTable>? = null, settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, configTables, settingBuilders, TomlFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.toml"))

}