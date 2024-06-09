package xd.arkosammy.monkeyconfig.tables

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.file.FileConfig
import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

abstract class MapConfigTable<V : SerializableType<*>>(defaultEntries: List<ConfigSetting<V, V>>, name: String, comment: String? = null) : AbstractConfigTable(name, comment, true, false) {

    override val configSettings: List<ConfigSetting<V, V>>
        get() = tableEntries

    private var tableEntries: List<ConfigSetting<V, V>> = defaultEntries
    private val defaultTableEntries: List<ConfigSetting<V, V>> = defaultEntries.toList()

    fun get(key: String) : V? {
        for(entry: ConfigSetting<V, V> in this.tableEntries) {
            if(entry.settingIdentifier.settingName == key) {
                return entry.value
            }
        }
        return null
    }

    override fun setDefaultValues(fileConfig: FileConfig) {
        val tempDefaultEntries: MutableList<ConfigSetting<V, V>> = mutableListOf()
        tempDefaultEntries.addAll(this.defaultTableEntries)
        this.tableEntries = tempDefaultEntries.toList()
        this.setValues(fileConfig)
    }

    override fun setValues(fileConfig: FileConfig) {
        if(this.tableEntries.isNotEmpty()) {
            for(setting: ConfigSetting<V, V> in this.tableEntries) {
                val settingAddress: String = "${this.name}.${setting.settingIdentifier.settingName}"
                val valueAsSerialized: V = setting.valueAsSerialized
                fileConfig.set<Any>(settingAddress, if(valueAsSerialized is ListType<*>) valueAsSerialized.listAsFullyDeserialized else valueAsSerialized.value)
            }
        }
        this.comment?.let { comment ->
            if(fileConfig is CommentedFileConfig) fileConfig.setComment(this.name, comment)
        }
    }

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
        this.tableEntries = tempEntries.toList()
    }

}