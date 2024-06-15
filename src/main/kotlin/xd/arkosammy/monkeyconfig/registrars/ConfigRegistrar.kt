package xd.arkosammy.monkeyconfig.registrars

import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * Represents a container of [ConfigManager] instances with methods
 * to call certain methods on the registered [ConfigManager]s in this [ConfigRegistrar].
 */
interface ConfigRegistrar {

    /**
     * Adds a [ConfigManager] to this [ConfigRegistrar].
     *
     * @param [configManager] The [ConfigManager] to register.
     */
    fun registerConfigManager(configManager: ConfigManager)

    /**
     * Invokes [ConfigManager.reloadFromFile] on all [ConfigManager] instances registered in this [ConfigRegistrar].
     * Immediately after that call, [onReloadedCallback] is invoked using the same [ConfigManager].
     *
     * @param [onReloadedCallback] The function to call after [ConfigManager.reloadFromFile] is invoked.
     */
    fun reloadAllManagers(onReloadedCallback: (ConfigManager) -> Unit = {})

    /**
     * Invokes [ConfigManager.saveToFile] on all [ConfigManager] instances registered in this [ConfigManager].
     * Immediately after that call, [onSavedCallback] is invoked using the same [ConfigManager].
     *
     * @param [onSavedCallback] The function to call after [ConfigManager.saveToFile] is invoked.
     */
    fun saveAllManagers(onSavedCallback: (ConfigManager) -> Unit = {})

    /**
     * Runs the given [action] on every [ConfigManager] registered on this [ConfigRegistrar]
     */
    fun forEachManager(action: (ConfigManager) -> Unit)

}