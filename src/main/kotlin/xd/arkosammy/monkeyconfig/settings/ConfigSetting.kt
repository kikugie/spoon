package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import xd.arkosammy.monkeyconfig.tables.ConfigTable
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * Represents a single configuration entry in a [ConfigTable].
 * While the value stored in a [ConfigSetting] can be anything,
 * since it needs to be serialized to and from a config file,
 * the value should be able to be converted to and from a [SerializableType]
 *
 * @param [T] The type of the value to store in this [ConfigSetting]
 * @param [S] The type to use to convert the value of [ConfigSetting] to a type that can be written to and from a config file.
 */
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

    /**
     * A builder class for a [ConfigSetting].
     * This is mainly used
     * to create [ConfigSetting] instances inside a [ConfigManager]
     * to ensure that only the manager has access to the actual [ConfigSetting] instance.
     *
     * @param [V] The type of the value which will be used in the resulting [ConfigSetting] instance
     * @param [S] The [SerializableType] type which will be used in the resulting [ConfigSetting]
     * @param [T] The type of the resulting [ConfigSetting] instance
     */
    abstract class Builder<V : Any, S : SerializableType<*>, T : ConfigSetting<V, S>> @JvmOverloads constructor(protected val id: SettingIdentifier, protected var comment: String? = null, protected val defaultValue: V) {

        val tableName: String
            get() = id.tableName

        abstract fun build() : T

    }

}