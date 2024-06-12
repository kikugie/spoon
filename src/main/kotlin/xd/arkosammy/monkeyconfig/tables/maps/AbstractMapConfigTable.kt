package xd.arkosammy.monkeyconfig.tables.maps

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
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
abstract class AbstractMapConfigTable<V : SerializableType<*>> @JvmOverloads constructor(
    defaultEntries: List<ConfigSetting<V, V>>,
    tableEntries: List<ConfigSetting<V, V>> = defaultEntries,
    override val name: String,
    override val comment: String? = null) : MapConfigTable<V> {

    override val configSettings: List<ConfigSetting<V, V>>
        get() = tableEntries.toList()

    protected var tableEntries: MutableList<ConfigSetting<V, V>> = tableEntries.toMutableList()
    protected val defaultTableEntries: List<ConfigSetting<V, V>> = defaultEntries.toList()
    protected var _isRegistered: Boolean = false
    override val isRegistered: Boolean
        get() = this._isRegistered

    override fun setAsRegistered() {
        this._isRegistered = true
    }

    override fun get(key: String) : V? {
        this.tableEntries.forEach { tableEntry ->
            if(tableEntry.settingIdentifier.settingName == key) {
                return tableEntry.value
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
                val valueAsSerialized: V = setting.serializedValue
                fileConfig.set<Any>(settingAddress, if(valueAsSerialized is ListType<*>) valueAsSerialized.fullyDeserializedList else valueAsSerialized.value)
            }
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
    }

    // TODO: Make sure this cast always succeeds
    @Suppress("UNCHECKED_CAST")
    override fun loadValues(fileConfig: FileConfig) {
        val config: Config = fileConfig.get(this.name)
        val tempEntries: MutableList<ConfigSetting<V, V>> = mutableListOf()
        for(entry: Config.Entry in config.entrySet()) {
            val readValue: Any = entry.getValue()
            val deserializedValue: SerializableType<*> = toSerializedType(readValue)

            // The following unsafe cast operation is done under the assumption
            // that this MapConfigTable instance wrote values of type V as their actual values.
            // If that's the case, then we should only read back values of type V
            // after the raw value is converted to its serialized form.
            // For example, if this object is an instance of MapConfigTable<StringType>,
            // then strings should be written to the config file under this table,
            // and then read back as strings, then mapped to StringType instances.
            // If that holds true, then the cast to V should be successful,
            // as StringType matches the type parameter of this MapConfigTable instance.
            try {
                val setting: ConfigSetting<V, V> = object : ConfigSetting<V, V>(SettingIdentifier(this.name, entry.key), defaultValue = deserializedValue as V) {
                    override val serializedValue: V
                        get() = this.value
                    override val serializedDefaultValue: V
                        get() = this.defaultValue

                    override fun setValueFromSerialized(serializedValue: V) {
                        this.value = serializedValue
                    }
                }
                tempEntries.add(setting)
            } catch (e: ClassCastException) {
                MonkeyConfig.LOGGER.error("Unexpected value \"$readValue\" of type \"${readValue::class.simpleName}\" found for ${this::class.simpleName} of name ${this.name}. This entry will not be added to this config table.")
            }
        }
        this.tableEntries.clear()
        this.tableEntries.addAll(tempEntries)
    }

    override fun toString(): String {
        return "${this::class.simpleName}{name=${this.name}, comment=${this.comment ?: "null"}, settingAmount=${this.configSettings.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave, registerSettingsAsCommands=$registerSettingsAsCommands}"
    }

}