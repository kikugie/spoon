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
abstract class AbstractMapSettingGroup<V : SerializableType<*>> @JvmOverloads constructor(
    override val name: String,
    override val comment: String? = null,
    defaultEntries: List<ConfigSetting<V, V>>,
    mapEntries: List<ConfigSetting<V, V>> = defaultEntries) : MapSettingGroup<V> {

    override val configSettings: List<ConfigSetting<V, V>>
        get() = mapEntries.toList()

    protected var mapEntries: MutableList<ConfigSetting<V, V>> = mapEntries.toMutableList()
    protected val defaultTableEntries: List<ConfigSetting<V, V>> = defaultEntries.toList()
    protected var _isRegistered: Boolean = false

    override val isRegistered: Boolean
        get() = this._isRegistered

    override fun setAsRegistered() {
        this._isRegistered = true
    }

    override fun get(key: String) : V? {
        this.mapEntries.forEach { tableEntry ->
            if(tableEntry.settingLocation.settingName == key) {
                return tableEntry.value
            }
        }
        return null
    }

    override fun setDefaultValues(fileConfig: FileConfig) {
        this.mapEntries.clear()
        this.mapEntries.addAll(this.defaultTableEntries)
        this.setValues(fileConfig)
    }

    override fun setValues(fileConfig: FileConfig) {
        if(this.mapEntries.isNotEmpty()) {
            for(setting: ConfigSetting<V, V> in this.mapEntries) {
                val settingAddress = "${this.name}.${setting.settingLocation.settingName}"
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
                val setting: ConfigSetting<V, V> = object : ConfigSetting<V, V>(SettingLocation(this.name, entry.key), defaultValue = deserializedValue as V) {
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
                MonkeyConfig.LOGGER.error("Unexpected value \"$readValue\" of type \"${readValue::class.simpleName}\" found for ${this::class.simpleName} of name ${this.name}. This entry will not be added to this setting group.")
            }
        }
        this.mapEntries.clear()
        this.mapEntries.addAll(tempEntries)
    }

    override fun toString(): String {
        return "${this::class.simpleName}{name=${this.name}, comment=${this.comment ?: "null"}, settingAmount=${this.configSettings.size}, registered=$isRegistered, loadBeforeSave=$loadBeforeSave, registerSettingsAsCommands=$registerSettingsAsCommands}"
    }

}