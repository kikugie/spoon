package xd.arkosammy.monkeyconfig.tables.maps

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.tables.AbstractConfigTable
import xd.arkosammy.monkeyconfig.tables.toSerializedType
import xd.arkosammy.monkeyconfig.tables.ConfigTable

// TODO: make default implementations of this class for users

/**
 * This implementation
 * of [ConfigTable] associates each of the [ConfigSetting] instances to its [SettingIdentifier] name.
 * All [ConfigSetting] instances stored in this table are written to and read from in bulk from the [FileConfig],
 * thus, it shouldn't be used to create [CommandControllableSetting] instances,
 * as the entries of this table can change during runtime by editing the config file.
 *
 * @param [V] The type of the values that will be written to and read from the [FileConfig].
 * must be an instance of [SerializableType]
 */
abstract class MapConfigTable<V : SerializableType<*>> @JvmOverloads constructor(defaultEntries: List<ConfigSetting<V, V>>, tableEntries: List<ConfigSetting<V, V>> = defaultEntries, name: String, comment: String? = null) : AbstractConfigTable(name, comment, true, false) {

    override val configSettings: List<ConfigSetting<V, V>>
        get() = tableEntries.toList()

    protected var tableEntries: MutableList<ConfigSetting<V, V>> = tableEntries.toMutableList()
    protected val defaultTableEntries: List<ConfigSetting<V, V>> = defaultEntries.toList()

    /**
     * Returns the [SerializableType] instance associated to the [key] parameter.
     *
     * @param key The [SettingIdentifier] name of the [ConfigSetting]
     * @return the [SerializableType] instance if found, or `null` otherwise
     */
    fun get(key: String) : V? {
        for(entry: ConfigSetting<V, V> in this.tableEntries) {
            if(entry.settingIdentifier.settingName == key) {
                return entry.value
            }
        }
        return null
    }

    override fun setDefaultValues(fileConfig: FileConfig) {
        this.tableEntries.clear()
        this.tableEntries.addAll(this.defaultTableEntries)
        this.setValues(fileConfig)
    }

    override fun setValues(fileConfig: FileConfig) {
        if(this.tableEntries.isNotEmpty()) {
            for(setting: ConfigSetting<V, V> in this.tableEntries) {
                val settingAddress = "${this.name}.${setting.settingIdentifier.settingName}"
                val valueAsSerialized: V = setting.valueAsSerialized
                fileConfig.set<Any>(settingAddress, if(valueAsSerialized is ListType<*>) valueAsSerialized.listAsFullyDeserialized else valueAsSerialized.value)
            }
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
    }

    // TODO: Make sure this cast always succeeds
    override fun loadValues(fileConfig: FileConfig) {
        val config: Config = fileConfig.get(this.name)
        val tempEntries: MutableList<ConfigSetting<V, V>> = mutableListOf()
        for(entry: Config.Entry in config.entrySet()) {
            val readValue: Any = entry.getValue()
            val deserializedValue: SerializableType<*> = toSerializedType(readValue)
            val setting: ConfigSetting<V, V> = object : ConfigSetting<V, V>(SettingIdentifier(this.name, entry.key), defaultValue = deserializedValue as V) {
                override val valueAsSerialized: V
                    get() = this.value
                override val defaultValueAsSerialized: V
                    get() = this.defaultValue
                override fun setFromSerializedValue(serializedValue: V) {
                    this.value = serializedValue
                }
            }
            tempEntries.add(setting)
        }
        this.tableEntries.clear()
        this.tableEntries.addAll(tempEntries)
    }

}