package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.ArgumentType
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.types.SerializableType
import xd.arkosammy.monkeyconfig.util.SettingLocation

abstract class AbstractCommandControllableSetting<T : Any, S : SerializableType<*>, A : ArgumentType<*>>(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: T,
    value: T = defaultValue) : ConfigSetting<T, S>(settingLocation, comment, defaultValue, value), CommandControllableSetting<T, A> {

    override val commandIdentifier: SettingLocation
        get() =  this.settingLocation

}