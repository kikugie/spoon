package xd.arkosammy.monkeyconfig.managers

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.ConfigFormat
import com.electronwill.nightconfig.core.file.FileConfig
import com.electronwill.nightconfig.core.file.GenericBuilder
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup
import xd.arkosammy.monkeyconfig.groups.DefaultMutableSettingGroup
import xd.arkosammy.monkeyconfig.util.SettingLocation
import java.nio.file.Files
import java.nio.file.Path
import xd.arkosammy.monkeyconfig.groups.DefaultSettingGroup

/**
 * The default implementation of [ConfigManager].
 * Developers should prefer to extend this class instead of implementing [ConfigManager],
 * as it contains most of the intended behavior of a [ConfigManager].
 * This class uses an internal [GenericBuilder] for a [FileConfig],
 * which is used to create the configuration file, and to write and read the [SettingGroup]s to and from it.
 */
abstract class AbstractConfigManager : ConfigManager {

    final override val settingGroups: List<SettingGroup>
    final override val configName: String
    protected val configBuilder: GenericBuilder<out Config, out FileConfig>
    protected val configFilePath: Path

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName] and [settingGroups].
     * The [SettingGroup]s given to this constructor must already be immutable and be populated with [ConfigSetting]s.
     *
     * @param [configName] The name of the configuration file that will be generated from this [ConfigManager]. In the files of the server. It will look like "[configName].$suffix".
     * @param [settingGroups] An immutable [List] of immutable [SettingGroup]s already populated with [ConfigSetting]s.
     * @param [configFormat] The [ConfigFormat] that will be used for the configuration file used by this [ConfigManager].
     * This mostly determines whether this configuration file supports comments.
     * @param [configPath] The [Path] of the configuration file used by this [ConfigManager].
     */
    constructor(configName: String, settingGroups: List<SettingGroup>, configFormat: ConfigFormat<*>, configPath: Path) {
        this.configName = configName
        this.configFilePath = configPath
        this.configBuilder = FileConfig.builder(this.configFilePath, configFormat)
            .preserveInsertionOrder()
            //.autoreload()
            .sync()
            //.onSave { this.onSave }
            //.onLoad { this.onLoaded }
            //.onAutoReload { this.onAutoReload }
        this.settingGroups = settingGroups.toList()
        this.initialize()
    }

    /**
     * Constructs a new [AbstractConfigManager] with the given [configName],
     * [settingGroups], [settingBuilders], [configFormat], and [configPath].
     * This constructor uses [ConfigSetting.Builder]s
     * to create [ConfigSetting] references that originate from this [ConfigManager],
     * and then added to the corresponding [SettingGroup] in [settingGroups].
     * If a [SettingGroup] is not found for a [ConfigSetting.Builder],
     * a new [DefaultSettingGroup] is created for it and added to this [ConfigManager].
     * Finally,
     * all [MutableSettingGroup] instances are converted to their immutable form by calling [MutableSettingGroup.toImmutable]
     *
     * @param [configName] The name of the configuration file that will be generated from this [ConfigManager]. In the files of the server. It will look like "[configName].$suffix".
     * @param [settingGroups] An immutable [List] of [MutableSettingGroup]s, which will be populated by [ConfigSetting]s.
     * @param [settingBuilders] A [List] of [ConfigSetting.Builder] that will be used to build the [ConfigSetting] with which to populate this [ConfigManager]'s [SettingGroup]s.
     * @param [configFormat] The [ConfigFormat] that will be used for the configuration file used by this [ConfigManager].
     * This mostly determines whether this configuration file supports comments.
     * @param [configPath] The [Path] of the configuration file used by this [ConfigManager].
     */
    constructor(configName: String, settingGroups: List<MutableSettingGroup>? = null, settingBuilders: List<ConfigSetting.Builder<*, *, *>>, configFormat: ConfigFormat<*>, configPath: Path) {
        this.configName = configName
        this.configFilePath = configPath
        this.configBuilder = FileConfig.builder(this.configFilePath, configFormat)
            .preserveInsertionOrder()
            //.autoreload()
            .sync()
            //.onSave { this.onSave }
            //.onLoad { this.onLoaded }
            //.onAutoReload { this.onAutoReload }

        val newGroups: MutableList<MutableSettingGroup> = settingGroups?.toMutableList() ?: mutableListOf()
        for(settingBuilder: ConfigSetting.Builder<*, *, *> in settingBuilders) {
            val settingGroupName: String = settingBuilder.settingLocation.groupName
            // Create a new DefaultSettingGroup if a setting group for this setting builder isn't found
            if(!newGroups.any { group -> group.name == settingGroupName}) {
                val newGroup: MutableSettingGroup = DefaultMutableSettingGroup(settingGroupName, configSettings = mutableListOf())
                MonkeyConfig.LOGGER.warn("Found no setting group for setting: ${settingBuilder.settingLocation.settingName}: A new setting group named $settingGroupName will be added to this ${this::class.simpleName} with name ${this.configName}")
                newGroups.add(newGroup)
            }
            for(settingGroup: MutableSettingGroup in newGroups) {
                if(settingGroup.name != settingGroupName) {
                    continue
                }
                settingGroup.addConfigSetting(settingBuilder.build())
            }
        }
        this.settingGroups = newGroups.toList().map(MutableSettingGroup::toImmutable)
        this.initialize()
    }

    private fun initialize() {
        System.setProperty("nightconfig.preserveInsertionOrder", "true")
        for (settingGroup: SettingGroup in this.settingGroups) {
            settingGroup.setAsRegistered()
            settingGroup.onRegistered()
        }
        this.checkForSettingNameUniqueness()
        this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            for (settingGroup: SettingGroup in this.settingGroups) {
                settingGroup.loadValues(fileConfig)
                settingGroup.onLoaded()
            }
            this.saveToFile()
            MonkeyConfig.LOGGER.info("Found existing configuration file for ${this::class.simpleName} with name ${this.configName}. Loaded values from ${this.configFilePath}")
            return@ifConfigPresent true
        }

    }

    override fun reloadFromFile(): Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            for (settingGroup: SettingGroup in this.settingGroups) {
                settingGroup.loadValues(fileConfig)
                settingGroup.onLoaded()
            }
            return@ifConfigPresent true
        }
    }

    override fun saveToFile() : Boolean {
        return this.ifConfigPresent { fileConfig ->
            fileConfig.load()
            for (settingGroup: SettingGroup in this.settingGroups) {
                if (settingGroup.loadBeforeSave) {
                    settingGroup.loadValues(fileConfig)
                }
                settingGroup.setValues(fileConfig)
            }
            // Remove unused setting groups and their settings
            fileConfig.entrySet().removeIf { settingGroupEntry ->
                if (!this.containsSettingGroupName(settingGroupEntry.key)) {
                    fileConfig.get<Config>(settingGroupEntry.key).entrySet().clear()
                    true
                } else {
                    false
                }
            }
            fileConfig.save()
            for(settingGroup: SettingGroup in this.settingGroups) {
                settingGroup.onSavedToFile()
            }
            return@ifConfigPresent true
        }
    }

    override fun getSettingGroup(groupName: String): SettingGroup? {
        for (settingGroup: SettingGroup in this.settingGroups) {
            if (settingGroup.name == groupName) {
                return settingGroup
            }
        }
        return null
    }

    // Not using reified generics to keep interoperability with Java and to avoid the need to expose the config tables
    @Suppress("UNCHECKED_CAST")
    override fun <V, T : ConfigSetting<V, *>> getTypedSetting(settingLocation: SettingLocation, settingClass: Class<T>): T? {
        val groupName: String = settingLocation.groupName
        val settingName: String = settingLocation.settingName
        for (settingGroup: SettingGroup in this.settingGroups) {
            if (settingGroup.name != groupName) {
                continue
            }
            // The following unchecked cast is done safely using the Class#isInstance method.
            // This checks for whether the current setting is an instance of the settingClass parameter,
            // which determines the type of the setting that the caller wants to retrieve
            for (setting: ConfigSetting<*, *> in settingGroup.configSettings) {
                if (!settingClass.isInstance(setting) || setting.settingLocation.settingName != settingName) {
                    continue
                }
                return setting as T
            }
        }
        return null
    }

    protected fun ifConfigPresent(fileConfigFunction: (FileConfig) -> Boolean): Boolean {
        val fileExists: Boolean = Files.exists(this.configFilePath)
        return this.configBuilder.build().use { fileConfig ->
            if(fileExists) {
                return@ifConfigPresent fileConfigFunction(fileConfig ?: return@ifConfigPresent false)
            } else {
                MonkeyConfig.LOGGER.warn("Found no preexisting configuration file for use. Creating a new configuration file at ${this.configFilePath}")
                this.createNewConfigFile(fileConfig)
                false
            }
        }
    }

    protected fun createNewConfigFile(fileConfig: FileConfig) {
        for (settingGroup: SettingGroup in this.settingGroups) {
            settingGroup.setDefaultValues(fileConfig)
        }
        fileConfig.save()
    }

    private fun checkForSettingNameUniqueness() {
        val settingNames: MutableSet<String> = mutableSetOf()
        for (settingGroup: SettingGroup in this.settingGroups) {
            for (setting: ConfigSetting<*, *> in settingGroup.configSettings) {
                if (settingNames.contains(setting.settingLocation.settingName)) {
                    throw IllegalArgumentException("Setting name ${setting.settingLocation} is not unique")
                }
                settingNames.add(setting.settingLocation.settingName)
            }
        }
    }

    override fun toString(): String {
        return "${this::class.simpleName}{name=${this.configName}, path=${this.configFilePath}, tableAmount=${this.settingGroups.size}}"
    }

}
