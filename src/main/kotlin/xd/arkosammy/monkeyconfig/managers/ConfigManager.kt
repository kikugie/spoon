package xd.arkosammy.monkeyconfig.managers

import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable
import xd.arkosammy.monkeyconfig.settings.list.StringListSetting
import kotlin.jvm.Throws

/**
 * A class that manages [ConfigTable] and serializes them to a configuration file for use by an external user.
 */
interface ConfigManager {

    /**
     * The name of the configuration file used by this [ConfigManager]. It should not include the file suffix.
     */
    val configName: String

    /**
     * An immutable list of the config tables in this config manager.
     * If this list is provided by an outside source,
     * implementors should copy this list into an immutable list to avoid further modifications.
     * Implementors should also make sure that the [ConfigTable] instances are not an instance of [MutableConfigTable].
     */
    val configTables: List<ConfigTable>

    /**
     * Will attempt
     * to reload the values of the config settings stored in this config manager from the file if it exists.
     *
     * @return `true` if the reload was successful, false if otherwise.
     * A common cause of the reloading failing is due to a missing config file.
     */
    fun reloadFromFile() : Boolean

    /**
     * Will attempt to save the values of the config settings stored in this config manager to the config file,
     * or create a new config file if it doesn't exist.
     * If a new config file is created, the config tables in this manager will be reset to their default values.
     */
    fun saveToFile()

    /**
     * Returns a config setting with the given [settingId] and [settingClass].
     * If the setting does not exist, an [IllegalArgumentException] will be thrown.
     */
    @Throws(IllegalArgumentException::class)
    fun <V, T : ConfigSetting<V, *>> getTypedSetting(settingId: SettingIdentifier, settingClass: Class<T>) : T

    /**
     * Returns a config table with the given [tableName], or throws an [IllegalArgumentException] if the table cannot be found.
     */
    @Throws(IllegalArgumentException::class)
    fun getConfigTable(tableName: String) : ConfigTable

}

inline fun <V, reified T : ConfigSetting<V, *>> ConfigManager.getTypedSetting(settingId: SettingIdentifier) : T {
    return this.getTypedSetting(settingId, T::class.java)
}

fun ConfigManager.getAsIntSetting(settingId: SettingIdentifier) : NumberSetting<Int> = this.getTypedSetting(settingId)

fun ConfigManager.getAsDoubleSetting(settingId: SettingIdentifier) : NumberSetting<Double> = this.getTypedSetting(settingId)

fun ConfigManager.getAsBooleanSetting(settingId: SettingIdentifier) : BooleanSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsStringSetting(settingId: SettingIdentifier) : StringSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsStringListSetting(settingId: SettingIdentifier) : StringListSetting = this.getTypedSetting(settingId)

