package xd.arkosammy.monkeyconfigtest

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.managers.TomlConfigManager
import xd.arkosammy.monkeyconfig.registrars.DefaultConfigRegistrar
import xd.arkosammy.monkeyconfig.settings.ConfigSetting

object KMonkeyConfigTest : ModInitializer {

    const val MOD_ID: String = "monkeyconfig-test"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val manager: ConfigManager = TomlConfigManager(MOD_ID, settingBuilders =  MyTestSettings.getBuilders() as List<ConfigSetting.Builder<*, *, *>>)

    override fun onInitialize() {

        DefaultConfigRegistrar.registerConfigManager(manager)

    }
}