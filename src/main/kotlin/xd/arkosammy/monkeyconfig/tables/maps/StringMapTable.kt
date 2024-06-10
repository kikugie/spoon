package xd.arkosammy.monkeyconfig.tables.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.StringType

// TODO: Test this
open class StringMapTable @JvmOverloads constructor(defaultEntries: List<ConfigSetting<StringType, StringType>>, tableEntries: List<ConfigSetting<StringType, StringType>> = defaultEntries, name: String, comment: String? = null) : MapConfigTable<StringType>(defaultEntries, tableEntries, name, comment)