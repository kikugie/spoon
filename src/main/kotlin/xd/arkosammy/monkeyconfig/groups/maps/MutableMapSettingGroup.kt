package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.groups.MutableSettingGroup
import xd.arkosammy.monkeyconfig.types.SerializableType

interface MutableMapSettingGroup<V, S : SerializableType<*>> : MapSettingGroup<V, S>, MutableSettingGroup {

    override val configSettings: MutableList<ConfigSetting<V, S>>

}