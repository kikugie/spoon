package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.managers.ConfigManager

/**
 * Represents a single configuration entry in a [SettingGroup].
 * While the value stored in a [ConfigSetting] can be anything,
 * since it needs to be serialized to and from a config file,
 * the value should be able to be converted to and from a [SerializableType].
 *
 * @param [T] The type of the value to store in this [ConfigSetting].
 * @param [S] The type to use to convert the value of [ConfigSetting] to a type that can be written to and from a config file.
 */
abstract class ConfigSetting<T, S : SerializableType<*>> @JvmOverloads constructor(
    open val settingLocation: SettingLocation,
    open val comment: String? = null,
    open val defaultValue: T,
    open var value: T = defaultValue) {

    abstract val serializedValue: S

    abstract val serializedDefaultValue: S

    abstract fun setValueFromSerialized(serializedValue: S)

    fun resetValue() {
        this.value = this.defaultValue
    }

    override fun toString(): String {
        return "${this::class.simpleName}{id=${this.settingLocation}, comment=${this.comment ?: "null"}, defaultValue=${this.defaultValue}}, value=${this.value}, serializedType=${this.serializedDefaultValue::class.simpleName}}"
    }

    /**
     * A builder class for a [ConfigSetting].
     * This is mainly used
     * to create [ConfigSetting] instances inside a [ConfigManager]
     * to ensure that only the manager has access to the actual [ConfigSetting] instance.
     *
     * @param [T] The type of the resulting [ConfigSetting] instance.
     * @param [V] The type of the value which will be used in the resulting [ConfigSetting] instance.
     * @param [S] The [SerializableType] type which will be used in the resulting [ConfigSetting].
     */
    abstract class Builder<T : ConfigSetting<V, S>, V : Any, S : SerializableType<*>> @JvmOverloads constructor(val settingLocation: SettingLocation, protected var comment: String? = null, val defaultValue: V) {

        val tableName: String
            get() = settingLocation.groupName

        abstract fun build() : T

    }

}