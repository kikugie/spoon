package xd.arkosammy.monkeyconfig.registrars

import xd.arkosammy.monkeyconfig.managers.ConfigManager

interface ConfigRegistrar {

    fun registerConfigManager(configManager: ConfigManager)

    fun reloadAllManagers(callback: (ConfigManager) -> Unit = {})

    fun saveAllManagers(callback: (ConfigManager) -> Unit = {})

}