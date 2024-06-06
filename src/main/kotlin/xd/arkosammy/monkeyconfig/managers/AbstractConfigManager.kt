package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import java.nio.file.Files
import java.nio.file.Path

/**
 * The default behavior implementation of the [ConfigManager] interface. This class is abstract and should be extended, as it contains the intended behavior of the [ConfigManager] interface.
 */
abstract class AbstractConfigManager : ConfigManager {

    final override val configTables: List<ConfigTable>
    final override val configName: String
    protected abstract val configBuilder: GenericBuilder<out Config, out FileConfig>
    protected abstract val configPath: Path

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName] and [configTables]. The [configTables] given to this constructor must already be immutable and be populated with [ConfigSetting]s.
     */
    constructor(configName: String, configTables: List<ConfigTable>) {
        this.configName = configName
        this.configTables = configTables.toList()
        this.initialize()
    }

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName], [configTables], and [settingBuilders]. The setting builders are used to create the settings for the config tables. The config tables are then turned to their immutable variation.
     */
    constructor(configName: String, configTables: List<MutableConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *>>) {
        this.configName = configName
        for (settingBuilder: ConfigSetting.Builder<*, *> in settingBuilders) {
            val tableName: String = settingBuilder.tableName
            for (configTable: MutableConfigTable in configTables) {
                if (configTable.name != tableName) {
                    continue
                }
                configTable.addConfigSetting(settingBuilder.build())
                break
            }
        }
        this.configTables = configTables.map(MutableConfigTable::toImmutable).toList()
        this.initialize()
    }

    private fun initialize() {
        this.configTables.forEach(ConfigTable::setAsRegistered)
        this.configTables.forEach(ConfigTable::onRegistered)
        System.setProperty("nightconfig.preserveInsertionOrder", "true")
        this.checkForSettingNameUniqueness()
        this.ifConfigPresent { fileConfig ->
            if (!Files.exists(this.configPath)) {
                MonkeyConfig.LOGGER.warn("Found no preexisting configuration file to load settings from. Creating a new configuration file with default values in ${this.configPath}")
                this.createNewConfigFile(fileConfig)
            } else {
                fileConfig.load()
                this.configTables.forEach { table -> table.loadValues(fileConfig) }
                this.configTables.forEach(ConfigTable::onLoaded)
                this.saveToFile()
                MonkeyConfig.LOGGER.info("Found existing configuration file. Loaded values from ${this.configPath}")
            }
            return@ifConfigPresent true
        }
    }

    override fun reloadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            if (!Files.exists(this.configPath)) {
                return@ifConfigPresent false
            }
            fileConfig.load()
            this.configTables.forEach { configTable -> configTable.loadValues(fileConfig) }
            this.configTables.forEach(ConfigTable::onLoaded)
            return@ifConfigPresent true
        }
    }

    override fun saveToFile() {
        this.ifConfigPresent { fileConfig ->
            if (!Files.exists(this.configPath)) {
                MonkeyConfig.LOGGER.warn("Found no preexisting configuration file to save settings to. Creating a new configuration file with default values in ${this.configPath}")
                this.createNewConfigFile(fileConfig)
            } else {
                fileConfig.load()
                this.configTables.forEach { table -> if (table.loadBeforeSave) table.loadValues(fileConfig) }
                this.configTables.forEach { table -> table.setValues(fileConfig) }
                fileConfig.save()
                this.configTables.forEach(ConfigTable::onSavedToFile)
            }
            return@ifConfigPresent true
        }
    }

    override fun getConfigTable(tableName: String): ConfigTable {
        for (configTable: ConfigTable in this.configTables) {
            if (configTable.name == tableName) {
                return configTable
            }
        }
        throw IllegalArgumentException("No table with name $tableName found")
    }

    // Not using reified generics to keep interoperability with Java and to avoid the need to expose the config tables
    @Suppress("UNCHECKED_CAST")
    override fun <V, T : ConfigSetting<V>> getTypedSetting(settingId: SettingIdentifier, settingClass: Class<T>): T {
        val tableName: String = settingId.tableName
        val settingName: String = settingId.settingName
        for (configTable: ConfigTable in this.configTables) {
            if (configTable.name != tableName) {
                continue
            }
            for (setting: ConfigSetting<*> in configTable.configSettings) {
                if (!settingClass.isInstance(setting) || setting.settingIdentifier.settingName != settingName) {
                    continue
                }
                return setting as T
            }
        }
        throw IllegalArgumentException("No setting with name ${settingId.settingName} found in table ${settingId.tableName}")
    }

    protected fun ifConfigPresent(fileConfigFunction: (FileConfig) -> Boolean): Boolean {
        this.configBuilder.build().use { fileConfig ->
            return@ifConfigPresent fileConfigFunction(fileConfig ?: return@ifConfigPresent false)
        }
    }

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        this.configTables.forEach { table -> table.setDefaultValues(fileConfig) }
        fileConfig.save()
    }

    private fun checkForSettingNameUniqueness() {
        val settingNames: MutableSet<String> = mutableSetOf()
        for (configTable: ConfigTable in this.configTables) {
            for (setting: ConfigSetting<*> in configTable.configSettings) {
                if (settingNames.contains(setting.settingIdentifier.settingName)) {
                    throw IllegalArgumentException("Setting name ${setting.settingIdentifier} is not unique")
                }
                settingNames.add(setting.settingIdentifier.settingName)
            }
        }
    }
}
