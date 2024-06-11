package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.json.JsonFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable

open class JsonConfigManager : AbstractConfigManager {

    constructor(configName: String, configTables: List<ConfigTable>) : super(configName, configTables, JsonFormat.fancyInstance(), FabricLoader.getInstance().configDir.resolve("$configName.json"))

    constructor(configName: String, configTables: List<MutableConfigTable>? = null, settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, configTables, settingBuilders, JsonFormat.fancyInstance(), FabricLoader.getInstance().configDir.resolve("$configName.json"))

}