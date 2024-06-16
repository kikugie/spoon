package xd.arkosammy.monkeyconfigtest

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xd.arkosammy.monkeyconfig.managers.*
import xd.arkosammy.monkeyconfig.registrars.DefaultConfigRegistrar
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.settings.NumberSetting
import xd.arkosammy.monkeyconfig.util.SettingLocation

object KMonkeyConfigTest : ModInitializer {

    const val MOD_ID: String = "monkeyconfig-test"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val tomlManager: ConfigManager = TomlConfigManager("$MOD_ID-toml", settingGroups = MyTestSettingGroups.getSettingGroups(), settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>> /*+ KMyTestSettings.getSettingBuilders()*/)
    val jsonManager: ConfigManager = JsonConfigManager("$MOD_ID-json", settingGroups = MyTestSettingGroups.getSettingGroups(), settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>> /*+ KMyTestSettings.getSettingBuilders()*/)
    val hoconManager: ConfigManager = HoconConfigManager("$MOD_ID-hocon", settingGroups = MyTestSettingGroups.getSettingGroups(), settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>> /*+ KMyTestSettings.getSettingBuilders()*/)
    //val yamlManager: ConfigManager = YamlConfigManager("$MOD_ID-yaml", settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)

    //val tomlManagerMapGroup: ConfigManager = TomlConfigManager("$MOD_ID-toml-map", MyTestSettingGroups.getSettingGroups())

    override fun onInitialize() {

        DefaultConfigRegistrar.registerConfigManager(tomlManager)
        DefaultConfigRegistrar.registerConfigManager(jsonManager)
        DefaultConfigRegistrar.registerConfigManager(hoconManager)
        //DefaultConfigRegistrar.registerConfigManager(tomlManagerMapGroup)
        //DefaultConfigRegistrar.registerConfigManager(yamlManager)

    }
}