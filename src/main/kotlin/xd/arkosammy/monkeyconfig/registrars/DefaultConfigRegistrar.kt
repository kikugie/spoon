package xd.arkosammy.monkeyconfig.registrars

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.commands.visitors.DefaultCommandVisitor
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.SettingGroup

/**
 * Default singleton implementation of [ConfigRegistrar].
 * This implementation generates commands for each [ConfigSetting]
 * contained in each [ConfigManager] registered via [ConfigRegistrar.registerConfigManager]
 * The registration of [ConfigManager] should only be done during mod initialization,
 * as this is the only time that commands can be normally registered.
 *
 * `MonkeyConfig` automatically calls [saveAllManagers] upon server shutdown on [DefaultConfigRegistrar].
 */
object DefaultConfigRegistrar : ConfigRegistrar {

    private val configManagers: MutableList<ConfigManager> = mutableListOf()

    override fun registerConfigManager(configManager: ConfigManager) {
        this.configManagers.add(configManager)
        CommandRegistrationCallback.EVENT.register { commandDispatcher, _, _ ->
            val commandVisitor: CommandVisitor = DefaultCommandVisitor(configManager, commandDispatcher = commandDispatcher)
            for(settingGroup: SettingGroup in configManager.settingGroups) {
                if(!settingGroup.registerSettingsAsCommands) {
                    continue
                }
                for(setting: ConfigSetting<*, *> in settingGroup.configSettings) {
                    if(setting !is CommandControllableSetting<*, *>) {
                        continue
                    }
                    setting.accept(commandVisitor)
                }
            }
        }
    }

    override fun reloadAllManagers(onReloadedCallback: (ConfigManager) -> Unit) {
        for(configManager: ConfigManager in this.configManagers) {
            configManager.reloadFromFile()
            onReloadedCallback(configManager)
        }
    }

    override fun saveAllManagers(onSavedCallback: (ConfigManager) -> Unit) {
        for(configManager: ConfigManager in this.configManagers) {
            configManager.saveToFile()
            onSavedCallback(configManager)
        }
    }

    override fun forEachManager(action: (ConfigManager) -> Unit) {
        for (configManager: ConfigManager in this.configManagers) {
            action(configManager)
        }
    }

}