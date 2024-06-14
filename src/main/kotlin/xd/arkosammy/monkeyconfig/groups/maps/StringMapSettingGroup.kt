package xd.arkosammy.monkeyconfig.groups.maps

import xd.arkosammy.monkeyconfig.settings.ConfigSetting
import xd.arkosammy.monkeyconfig.types.StringType

// TODO: Test this
open class StringMapSettingGroup @JvmOverloads constructor(
    name: String,
    comment: String? = null,
    defaultEntries: List<ConfigSetting<StringType, StringType>>,
    mapEntries: List<ConfigSetting<StringType, StringType>> = defaultEntries) : AbstractMapSettingGroup<StringType>(name, comment, defaultEntries, mapEntries)