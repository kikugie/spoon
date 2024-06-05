package xd.arkosammy.monkeyconfig.managers

import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import java.nio.file.Files

class BaseConfigManager(configName: String, configTables: List<ConfigTable>, settingBuilders: List<ConfigSetting.Builder<*, *>>) : AbstractConfigManager(configName, configTables, settingBuilders) {

    override fun reloadFromFile() : Boolean {
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

    override fun saveToFile() {
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

}