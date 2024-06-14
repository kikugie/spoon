package xd.arkosammy.monkeyconfig.managers

import xd.arkosammy.monkeyconfig.settings.*
import xd.arkosammy.monkeyconfig.groups.SettingGroup
import xd.arkosammy.monkeyconfig.util.SettingLocation
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup
import xd.arkosammy.monkeyconfig.settings.list.StringListSetting

/**
 * A manager of [SettingGroup]s and is in charge
 * of serializing the contained [SettingGroup] to and from a configuration file.
 */
interface ConfigManager {

    /**
     * The name of the configuration file used by this [ConfigManager]. It should not include the file suffix.
     */
    val configName: String

    /**
     * An immutable list of the [SettingGroup]s.
     * If this list is provided by an outside source,
     * implementors should copy this list into an immutable list to avoid further modifications.
     * Implementors should also make sure that the [SettingGroup] instances are not an instance of [MutableSettingGroup],
     * otherwise, use [MutableSettingGroup.toImmutable] to convert them to their immutable version.
     */
    val settingGroups: List<SettingGroup>

    /**
     * Will attempt
     * to update the values of the [ConfigSetting] in this manager's
     * [settingGroups] by reading from a configuration file,
     * if it exists.
     *
     * @return `true` if the reload was successful, `false` if otherwise.
     * A common cause of failure is due to a missing configuration file.
     */
    fun reloadFromFile() : Boolean

    /**
     * Will attempt
     * to save the values of the [ConfigSetting] in this manager's
     * [settingGroups] by writing them to a configuration file.
     * If the configuration file is missing, a new configuration file will be created,
     * the values of the [ConfigSetting]s will be reset, and then written to the file.
     *
     * @return `true` if the save was successful, `false` otherwise.
     * A common cause of failure is due to a missing configuration file.
     */
    fun saveToFile() : Boolean

    /**
     * Returns a [ConfigSetting] with the given [settingLocation] and [settingClass].
     *
     * @param settingLocation The [SettingLocation] to look for in this [ConfigManager].
     * @param [settingClass] The class that represents the type of the [ConfigSetting]'s value to look for in this [ConfigManager].
     * @return the [ConfigSetting] given by the [settingLocation] and [settingClass],
     * or `null` if it wasn't found in this [ConfigManager]
     */
    fun <V, T : ConfigSetting<V, *>> getTypedSetting(settingLocation: SettingLocation, settingClass: Class<T>) : T?

    /**
     * Returns a [SettingGroup] with the given [groupName].
     * @param groupName The name of the [SettingGroup] to look for in this [ConfigManager].
     * @return a [SettingGroup] with a matching [groupName], or `null` if none were found.
     */
    fun getSettingGroup(groupName: String) : SettingGroup?

}

inline fun <V, reified T : ConfigSetting<V, *>> ConfigManager.getTypedSetting(settingId: SettingLocation) : T? {
    return this.getTypedSetting(settingId, T::class.java)
}

fun ConfigManager.getAsIntSetting(settingId: SettingLocation) : NumberSetting<Int>? = this.getTypedSetting<Int, NumberSetting<Int>>(settingId)

fun ConfigManager.getAsDoubleSetting(settingId: SettingLocation) : NumberSetting<Double>? = this.getTypedSetting<Double, NumberSetting<Double>>(settingId)

fun ConfigManager.getAsBooleanSetting(settingId: SettingLocation) : BooleanSetting? = this.getTypedSetting<Boolean, BooleanSetting>(settingId)

fun ConfigManager.getAsStringSetting(settingId: SettingLocation) : StringSetting? = this.getTypedSetting<String, StringSetting>(settingId)

fun ConfigManager.getAsStringListSetting(settingId: SettingLocation) : StringListSetting? = this.getTypedSetting<List<String>, StringListSetting>(settingId)