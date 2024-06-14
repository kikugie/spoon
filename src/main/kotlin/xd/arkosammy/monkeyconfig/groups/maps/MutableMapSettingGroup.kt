package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup
import xd.arkosammy.monkeyconfig.types.SerializableType

interface MutableMapSettingGroup<V : SerializableType<*>> : MapSettingGroup<V>, MutableSettingGroup {

    override val configSettings: MutableList<ConfigSetting<V, V>>

}