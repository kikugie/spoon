package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation

/**
 * A [ConfigSetting] which stores a [List] of values.
 * Implementors should feel free to override this class and provide a version that exposes a [MutableList] if necessary.
 * Implementors should keep in mind that when overriding [ConfigSetting.setValueFromSerialized],
 * the input [SerializableType] type instance will be a [ListType] whose elements are also of a [SerializableType],
 * so you must manually convert each element of the [ListType] back to the actual type.
 *
 * @param [E] The type of the elements in this [ListSetting]
 * @param [S] The type to use to serialize the elements of this list to and from a config file.
 * [S] will become the type parameter of [ListType] used to serialize the elements of this list.
 */
abstract class ListSetting<E, S : SerializableType<*>> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: List<E>,
    value: List<E> = defaultValue) : ConfigSetting<List<E>, ListType<S>>(settingLocation, comment, value) {

    abstract class Builder<E, S : SerializableType<*>> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: List<E>) : ConfigSetting.Builder<ListSetting<E, S>, List<E>, ListType<S>>(settingLocation, comment, defaultValue) {

        abstract override fun build(): ListSetting<E, S>

    }

}