package xd.arkosammy.monkeyconfig.managers

import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

/**
 * A class that handles the management of config tables and interacts with an internal [Config] instance to save and load settings from a file.
 */
interface ConfigManager {

    /**
     * A immutable view of the config tables in this config manager. If this list is provided by a mutable source, implementors should copy this list into an immutable list to avoid further modifications
     */
    val configTables: List<ConfigTable>

    fun reloadFromFile() : Boolean

    fun saveToFile()

    fun <V, T : ConfigSetting<V>> getTypedSetting(settingId: SettingIdentifier, settingClass: Class<T>) : T

    fun getConfigTable(tableName: String) : ConfigTable

}

inline fun <V, reified T : ConfigSetting<V>> ConfigManager.getTypedSetting(settingId: SettingIdentifier) : T {
    return this.getTypedSetting(settingId, T::class.java)
}

fun ConfigManager.getAsIntSetting(settingId: SettingIdentifier) : IntSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsDoubleSetting(settingId: SettingIdentifier) : DoubleSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsBooleanSetting(settingId: SettingIdentifier) : BooleanSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsStringSetting(settingId: SettingIdentifier) : StringSetting = this.getTypedSetting(settingId)

fun ConfigManager.getAsStringListSetting(settingId: SettingIdentifier) : StringListSetting = this.getTypedSetting(settingId)

