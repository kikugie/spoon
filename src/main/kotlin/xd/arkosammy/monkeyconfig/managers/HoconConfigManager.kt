package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.hocon.HoconFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable

open class HoconConfigManager : AbstractConfigManager {

    constructor(configName: String, configTables: List<ConfigTable>) : super(configName, configTables, HoconFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.conf"))

    constructor(configName: String, configTables: List<MutableConfigTable>? = null, settingBuilders: List<ConfigSetting.Builder<*, *, *>>) : super(configName, configTables, settingBuilders, HoconFormat.instance(), FabricLoader.getInstance().configDir.resolve("$configName.conf"))

}