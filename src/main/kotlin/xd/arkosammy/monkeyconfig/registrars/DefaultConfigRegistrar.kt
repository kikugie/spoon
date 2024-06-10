package xd.arkosammy.monkeyconfig.registrars

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.commands.visitors.DefaultCommandVisitor
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable

object DefaultConfigRegistrar : ConfigRegistrar {

    private val configManagers: MutableList<ConfigManager> = mutableListOf()

    override fun registerConfigManager(configManager: ConfigManager) {
        this.configManagers.add(configManager)
        CommandRegistrationCallback.EVENT.register { commandDispatcher, commandRegistryAccess, registrationEnvironment ->
            val commandVisitor: CommandVisitor = DefaultCommandVisitor(configManager, commandDispatcher = commandDispatcher)
            for(table: ConfigTable in configManager.configTables) {
                if(!table.registerSettingsAsCommands) {
                    continue
                }
                for(setting: ConfigSetting<*, *> in table.configSettings) {
                    if(setting !is CommandControllableSetting<*, *>) {
                        continue
                    }
                    setting.accept(commandVisitor)
                }
            }

        }
    }

    override fun reloadAllManagers(callback: (ConfigManager) -> Unit) {
        this.configManagers.forEach {manager ->
            manager.reloadFromFile()
            callback(manager)
        }
    }

    override fun saveAllManagers(callback: (ConfigManager) -> Unit) {
        this.configManagers.forEach { manager ->
            manager.saveToFile()
            callback(manager)
        }
    }
}