package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
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
 * This class uses an internal [GenericBuilder] for a [FileConfig], which is used to create the configuration file, and to write and read the [ConfigTable] to and from it.
 */
abstract class AbstractConfigManager : ConfigManager {

    final override val configTables: List<ConfigTable>
    final override val configName: String
    protected val configBuilder: GenericBuilder<out Config, out FileConfig>
    protected val configPath: Path
    // TODO: This is not being called
    /*
    protected open val onAutoReload: () -> Unit
        get() = {
            this.ifConfigPresent { fileConfig ->
                if (!Files.exists(this.configPath)) {
                    return@ifConfigPresent false
                }
                this.configTables.forEach { configTable -> configTable.loadValues(fileConfig) }
                this.configTables.forEach(ConfigTable::onLoaded)
                return@ifConfigPresent true
            }
        }
    protected open val onSave: () -> Unit
        get() = {}
    protected open val onLoaded: () -> Unit
        get() = {}

     */

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName] and [configTables]. The [configTables] given to this constructor must already be immutable and be populated with [ConfigSetting]s.
     */
    constructor(configName: String, configTables: List<ConfigTable>, configFormat: ConfigFormat<*>, configPath: Path) {
        this.configName = configName
        this.configPath = configPath
        this.configBuilder = FileConfig.builder(this.configPath, configFormat)
            .preserveInsertionOrder()
            //.autoreload()
            .sync()
            //.onSave { this.onSave }
            //.onLoad { this.onLoaded }
            //.onAutoReload { this.onAutoReload }
        this.configTables = configTables.toList()
        this.initialize()
    }

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName], [configTables], and [settingBuilders]. The setting builders are used to create the settings for the config tables. The config tables are then turned to their immutable variation.
     */
    constructor(configName: String, configTables: List<MutableConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *, *>>, configFormat: ConfigFormat<*>, configPath: Path) {
        this.configName = configName
        this.configPath = configPath
        this.configBuilder = FileConfig.builder(this.configPath, configFormat)
            .preserveInsertionOrder()
            //.autoreload()
            .sync()
            //.onSave { this.onSave }
            //.onLoad { this.onLoaded }
            //.onAutoReload { this.onAutoReload }
        for (settingBuilder: ConfigSetting.Builder<*, *, *> in settingBuilders) {
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
            fileConfig.load()
            this.configTables.forEach { table -> table.loadValues(fileConfig) }
            this.configTables.forEach(ConfigTable::onLoaded)
            this.saveToFile()
            MonkeyConfig.LOGGER.info("Found existing configuration file. Loaded values from ${this.configPath}")
            return@ifConfigPresent true
        }

    }

    override fun reloadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.configTables.forEach { configTable -> configTable.loadValues(fileConfig) }
            this.configTables.forEach(ConfigTable::onLoaded)
            return@ifConfigPresent true
        }
    }

    override fun saveToFile() {
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            this.configTables.forEach { table -> if (table.loadBeforeSave) table.loadValues(fileConfig) }
            this.configTables.forEach { table -> table.setValues(fileConfig) }
            fileConfig.save()
            this.configTables.forEach(ConfigTable::onSavedToFile)
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
    override fun <V, T : ConfigSetting<V, *>> getTypedSetting(settingId: SettingIdentifier, settingClass: Class<T>): T {
        val tableName: String = settingId.tableName
        val settingName: String = settingId.settingName
        for (configTable: ConfigTable in this.configTables) {
            if (configTable.name != tableName) {
                continue
            }
            for (setting: ConfigSetting<*, *> in configTable.configSettings) {
                if (!settingClass.isInstance(setting) || setting.settingIdentifier.settingName != settingName) {
                    continue
                }
                return setting as T
            }
        }
        throw IllegalArgumentException("No setting with name ${settingId.settingName} found in table ${settingId.tableName}")
    }

    protected fun ifConfigPresent(fileConfigFunction: (FileConfig) -> Boolean): Boolean {
        val fileExists: Boolean = Files.exists(this.configPath)
        return this.configBuilder.build().use { fileConfig ->
            if(fileExists) {
                return@ifConfigPresent fileConfigFunction(fileConfig ?: return@ifConfigPresent false)
            } else {
                MonkeyConfig.LOGGER.warn("Found no preexisting configuration file to load values from. Creating a new configuration file at ${this.configPath}")
                this.createNewConfigFile(fileConfig)
                false
            }
        }
    }

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        this.configTables.forEach { table -> table.setDefaultValues(fileConfig) }
        fileConfig.save()
    }

    private fun checkForSettingNameUniqueness() {
        val settingNames: MutableSet<String> = mutableSetOf()
        for (configTable: ConfigTable in this.configTables) {
            for (setting: ConfigSetting<*, *> in configTable.configSettings) {
                if (settingNames.contains(setting.settingIdentifier.settingName)) {
                    throw IllegalArgumentException("Setting name ${setting.settingIdentifier} is not unique")
                }
                settingNames.add(setting.settingIdentifier.settingName)
            }
        }
    }
}
