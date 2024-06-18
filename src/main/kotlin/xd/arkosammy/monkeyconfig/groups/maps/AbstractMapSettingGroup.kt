package xd.arkosammy.monkeyconfig.groups.maps

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation
import xd.arkosammy.monkeyconfig.types.toSerializedType

/**
 * Base implementation of [MapSettingGroup].
 * This class should be used as a reference for how to implement a custom [MapSettingGroup].
 */
abstract class AbstractMapSettingGroup<V : Any, S : SerializableType<*>> @JvmOverloads constructor(
    final override val name: String,
    override val comment: String? = null,
    defaultEntries: Map<String, V>,
    mapEntries: Map<String, V> = defaultEntries) : MapSettingGroup<V, S> {

    constructor(name: String, comment: String?, defaultEntries: List<ConfigSetting<V, S>>, entries: List<ConfigSetting<V, S>> = defaultEntries) : this(name, comment,
        defaultEntries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }, entries.associate { setting ->
            Pair(setting.settingLocation.settingName, setting.value)
        }
    )

    override val configSettings: List<ConfigSetting<V, S>>
        get() = mapEntries.toList()

    protected var mapEntries: MutableList<ConfigSetting<V, S>>
    protected val defaultTableEntries: List<ConfigSetting<V, S>>
    protected var _isRegistered: Boolean = false

    init {
        val settingEntries: MutableList<ConfigSetting<V, S>> = mutableListOf()
        for((key, value) in mapEntries) {
            val newMapEntry: ConfigSetting<V, S> = this.getMapEntryFromValue(SettingLocation(this.name, key), value)
            settingEntries.add(newMapEntry)
        }

        val defaultSettingEntries: MutableList<ConfigSetting<V, S>> = mutableListOf()
        for((key, value) in defaultEntries) {
            val newDefaultMapEntry: ConfigSetting<V, S> = this.getMapEntryFromValue(SettingLocation(this.name, key), value)
            defaultSettingEntries.add(newDefaultMapEntry)
        }
        this.mapEntries = settingEntries
        this.defaultTableEntries = defaultSettingEntries
    }

    override val isRegistered: Boolean
        get() = this._isRegistered

    override fun setAsRegistered() {
        this._isRegistered = true
    }

    final override val registerSettingsAsCommands: Boolean
        get() = false

    final override val loadBeforeSave: Boolean
        get() = true

    override fun get(key: String) : V? {
        for (mapEntry: ConfigSetting<V, S> in this.mapEntries) {
            if (mapEntry.settingLocation.settingName == key) {
                return mapEntry.value
            }
        }
        return null
    }

    override fun contains(key: String): Boolean {
        for (mapEntry: ConfigSetting<V, S> in this.mapEntries) {
            if (mapEntry.settingLocation.settingName == key) {
                return true
            }
        }
        return false
    }

    override fun setDefaultValues(fileConfig: FileConfig) {
        this.mapEntries.clear()
        this.mapEntries.addAll(this.defaultTableEntries)
        this.setValues(fileConfig)
    }

    override fun setValues(fileConfig: FileConfig) {
        for(setting: ConfigSetting<V, S> in this.mapEntries) {
            val settingAddress = "${this.name}.${setting.settingLocation.settingName}"
            val valueAsSerialized: S = setting.serializedValue
            fileConfig.set<Any>(settingAddress, if(valueAsSerialized is ListType<*>) valueAsSerialized.rawList else valueAsSerialized.rawValue)
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
    }

    override fun loadValues(fileConfig: FileConfig) {
        val config: Config = fileConfig.get(this.name) ?: run {
            MonkeyConfig.LOGGER.error("Found no SettingGroup with name ${this.name} to load values from!")
            return
        }
        val tempEntries: MutableList<ConfigSetting<V, S>> = mutableListOf()
        for(entry: Config.Entry in config.entrySet()) {
            val rawEntryValue: Any = entry.getValue()
            val serializedEntryValue: SerializableType<*> = toSerializedType(rawEntryValue)
            val newMapEntry: ConfigSetting<V, S>? = this.getMapEntryFromSerialized(SettingLocation(this.name, entry.key), serializedEntryValue)
            if (newMapEntry == null) {
                MonkeyConfig.LOGGER.error("Unable to read value of entry ${entry.key} from MapSettingGroup ${this.name}! This entry will be skipped.")
                continue
            }
            tempEntries.add(newMapEntry)
        }
        this.mapEntries.clear()
        this.mapEntries.addAll(tempEntries)
    }

    protected abstract fun getMapEntryFromSerialized(settingLocation: SettingLocation, serializedEntry: SerializableType<*>) : ConfigSetting<V, S>?

    protected abstract fun getMapEntryFromValue(settingLocation: SettingLocation, defaultValue: V, value: V = defaultValue) : ConfigSetting<V, S>

    override fun toString(): String =
        "${this::class.simpleName}{name=${this.name}, comment=${this.comment ?: "null"}, settingAmount=${this.configSettings.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave, registerSettingsAsCommands=$registerSettingsAsCommands}"

}