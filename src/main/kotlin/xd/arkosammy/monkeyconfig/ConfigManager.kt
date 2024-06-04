package xd.arkosammy.monkeyconfig

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import com.electronwill.nightconfig.toml.TomlFormat
import net.fabricmc.loader.api.FabricLoader
import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

class ConfigManager(configName: String, configTables: List<ConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *>>) {

    private val configPath: Path = FabricLoader.getInstance().configDir.resolve("$configName.toml")
    private val configTables: List<ConfigTable> = configTables.toList()
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
        for(settingBuilder: ConfigSetting.Builder<*, *> in settingBuilders) {
            val tableName: String = settingBuilder.tableName
            for(configTable: ConfigTable in this.configTables) {
                if(configTable.name != tableName) {
                    continue
                }
                configTable.addConfigSetting(settingBuilder.build())
                break
            }
        }
        this.configTables.forEach(ConfigTable::setAsRegistered)
        this.configTables.forEach(ConfigTable::onRegistered)
    }

    private fun ifConfigPresent(fileConfigFunction: (CommentedFileConfig) -> Boolean) : Boolean {
        this.configBuilder.build().use { fileConfig ->
            return@ifConfigPresent fileConfigFunction(fileConfig ?: return@ifConfigPresent false)
        }
    }

    fun reloadFromFile() : Boolean {
        return this.ifConfigPresent { fileConfig ->
            if(!Files.exists(this.configPath)) {
                return@ifConfigPresent false
            }
            fileConfig.load()
            this.configTables.forEach { configTable -> configTable.loadValues(fileConfig) }
            this.configTables.forEach(ConfigTable::onLoaded)
            return@ifConfigPresent true
        }
    }

    fun saveToFile() {
        this.ifConfigPresent { fileConfig ->
            if(!Files.exists(this.configPath)) {
                MonkeyConfig.LOGGER.warn("Found no preexisting configuration file to save settings to. Creating a new configuraiton fil ewith default values in ${this.configPath}")
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

    fun getConfigTable(tableName: String) : ConfigTable {
        for(configTable : ConfigTable in this.configTables) {
            if(configTable.name == tableName) {
                return configTable
            }
        }
        throw IllegalArgumentException("No table with name $tableName found")
    }

    inline fun <V, reified T : ConfigSetting<V>> getTypedSetting(settingId: SettingIdentifier) : T {
        return this.getTypedSetting(settingId, T::class)
    }

    // Not using reified generics to keep interoperability with Java and to avoid the need to expose the config tables
    @Suppress("UNCHECKED_CAST")
    fun <V, T : ConfigSetting<V>> getTypedSetting(settingId: SettingIdentifier, settingClass: KClass<T>) : T {
        val tableName: String = settingId.tableName
        val settingName: String = settingId.settingName
        for(configTable: ConfigTable in this.configTables) {
            if(configTable.name != tableName) {
                continue
            }
            for(setting: ConfigSetting<*> in configTable.configSettings) {
                if(!settingClass.isInstance(setting) || setting.name != settingName) {
                    continue
                }
                return setting as T
            }

        }
        throw IllegalArgumentException("No setting with name ${settingId.settingName} found in table ${settingId.tableName}")
    }

    fun getAsIntSetting(settingId: SettingIdentifier) : IntSetting = this.getTypedSetting(settingId)

    fun getAsDoubleSetting(settingId: SettingIdentifier) : DoubleSetting = this.getTypedSetting(settingId)

    fun getAsBooleanSetting(settingId: SettingIdentifier) : BooleanSetting = this.getTypedSetting(settingId)

    fun getAsStringSetting(settingId: SettingIdentifier) : StringSetting = this.getTypedSetting(settingId)

    fun getAsStringListSetting(settingId: SettingIdentifier) : StringListSetting = this.getTypedSetting(settingId)

    private fun createNewConfigFile(fileConfig: CommentedFileConfig) {
        this.configTables.forEach {table -> table.setDefaultValues(fileConfig)}
        fileConfig.save()
    }

    private fun checkForSettingNameUniqueness() {
        val settingNames: MutableSet<String> = HashSet()
        for(configTable: ConfigTable in this.configTables) {
            for(setting: ConfigSetting<*> in configTable.configSettings) {
                if(settingNames.contains(setting.name)) {
                    throw IllegalArgumentException("Setting name ${setting.name} is not unique")
                }
                settingNames.add(setting.name)
            }
        }
    }

}