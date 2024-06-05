package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import com.electronwill.nightconfig.toml.TomlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import java.nio.file.Files
import java.nio.file.Path

abstract class AbstractConfigManager(configName: String, configTables: List<ConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *>>) : ConfigManager {

    protected val configPath: Path = FabricLoader.getInstance().configDir.resolve("$configName.toml")
    override val configTables: List<ConfigTable> = configTables.toList()
    private var configBuilder: GenericBuilder<CommentedConfig, CommentedFileConfig> = CommentedFileConfig.builder(this.configPath, TomlFormat.instance())
        .preserveInsertionOrder()
        .concurrent()
        .sync()

    init {
        System.setProperty("nightconfig.preserveInsertionOrder", "true")
        this.registerConfigSettings(settingBuilders)
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

    private fun registerConfigSettings(settingBuilders: List<ConfigSetting.Builder<*, *>>) {
        settingBuilders.map { settingBuilder -> settingBuilder.build(this) }
            .groupBy { setting -> setting.settingIdentifier.tableName }.forEach { (tableName, settings) ->
                for (configTable: ConfigTable in this.configTables) {
                    if (configTable.name != tableName) {
                        continue
                    }
                    configTable.addConfigSettings(settings)
                    break
                }
                this.configTables.forEach(ConfigTable::setAsRegistered)
                this.configTables.forEach(ConfigTable::onRegistered)
            }

    }

    protected fun ifConfigPresent(fileConfigFunction: (CommentedFileConfig) -> Boolean) : Boolean {
        this.configBuilder.build().use { fileConfig ->
            return@ifConfigPresent fileConfigFunction(fileConfig ?: return@ifConfigPresent false)
        }
    }

    abstract override fun reloadFromFile() : Boolean

    abstract override fun saveToFile()

    override fun getConfigTable(tableName: String) : ConfigTable {
        for(configTable : ConfigTable in this.configTables) {
            if(configTable.name == tableName) {
                return configTable
            }
        }
        throw IllegalArgumentException("No table with name $tableName found")
    }

    // Not using reified generics to keep interoperability with Java and to avoid the need to expose the config tables
    @Suppress("UNCHECKED_CAST")
    override fun <V, T : ConfigSetting<V>> getTypedSetting(settingId: SettingIdentifier, settingClass: Class<T>) : T {
        val tableName: String = settingId.tableName
        val settingName: String = settingId.settingName
        for(configTable: ConfigTable in this.configTables) {
            if(configTable.name != tableName) {
                continue
            }
            for(setting: ConfigSetting<*> in configTable.configSettings) {
                if(!settingClass.isInstance(setting) || setting.settingIdentifier.settingName != settingName) {
                    continue
                }
                return setting as T
            }

        }
        throw IllegalArgumentException("No setting with name ${settingId.settingName} found in table ${settingId.tableName}")
    }

    protected fun createNewConfigFile(fileConfig: CommentedFileConfig) {
        this.configTables.forEach {table -> table.setDefaultValues(fileConfig)}
        fileConfig.save()
    }

    private fun checkForSettingNameUniqueness() {
        val settingNames: MutableSet<String> = HashSet()
        for(configTable: ConfigTable in this.configTables) {
            for(setting: ConfigSetting<*> in configTable.configSettings) {
                if(settingNames.contains(setting.settingIdentifier.settingName)) {
                    throw IllegalArgumentException("Setting name ${setting.settingIdentifier} is not unique")
                }
                settingNames.add(setting.settingIdentifier.settingName)
            }
        }
    }
}