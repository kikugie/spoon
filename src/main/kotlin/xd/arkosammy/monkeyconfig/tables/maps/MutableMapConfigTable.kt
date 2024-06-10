package xd.arkosammy.monkeyconfig.tables.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.tables.MutableConfigTable
import xd.arkosammy.monkeyconfig.types.SerializableType

interface MutableMapConfigTable<V : SerializableType<*>> : MutableConfigTable {

    override val configSettings: MutableList<ConfigSetting<V, V>>

}