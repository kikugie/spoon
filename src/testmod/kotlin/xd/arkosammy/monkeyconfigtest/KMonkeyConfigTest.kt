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
    val tomlManager: ConfigManager = TomlConfigManager("$MOD_ID-toml", settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)
    val jsonManager: ConfigManager = JsonConfigManager("$MOD_ID-json", settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)
    val hoconManager: ConfigManager = HoconConfigManager("$MOD_ID-hocon", settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)
    val yamlManager: ConfigManager = YamlConfigManager("$MOD_ID-yaml", settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)


    override fun onInitialize() {

        DefaultConfigRegistrar.registerConfigManager(tomlManager)
        DefaultConfigRegistrar.registerConfigManager(jsonManager)
        DefaultConfigRegistrar.registerConfigManager(hoconManager)
        DefaultConfigRegistrar.registerConfigManager(yamlManager)

        val numberSetting: NumberSetting<Int>? = tomlManager.getTypedSetting<Int, NumberSetting<Int>>(SettingLocation("a", "o"))


    }
}