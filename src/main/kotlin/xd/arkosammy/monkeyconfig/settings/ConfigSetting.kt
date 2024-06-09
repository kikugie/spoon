package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

abstract class ConfigSetting<T, S : SerializableType<*>> @JvmOverloads constructor(
    open val settingIdentifier: SettingIdentifier,
    open val comment: String? = null,
    open val defaultValue: T,
    open var value: T = defaultValue) {

    abstract val valueAsSerialized: S

    abstract val defaultValueAsSerialized: S

    abstract fun setFromSerializedValue(serializedValue: S)

    fun resetValue() {
        this.value = this.defaultValue
    }

    abstract class Builder<V : Any, S : SerializableType<*>, T : ConfigSetting<V, S>>(protected val id: SettingIdentifier, protected var comment: String? = null, protected val defaultValue: V) {

        val tableName: String
            get() = id.tableName

        abstract fun build() : T

    }

}