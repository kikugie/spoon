package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.util.SettingLocation

open class BooleanListSetting @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: List<Boolean>,
    value: List<Boolean> = defaultValue) : ListSetting<Boolean, BooleanType>(settingLocation, comment, defaultValue, value) {

    override val valueToSerializedConverter: (List<Boolean>) -> ListType<BooleanType>
        get() = { booleanList -> ListType(booleanList.toList().map{ e -> BooleanType(e) }) }

    override val serializedToValueConverter: (ListType<BooleanType>) -> List<Boolean>
        get() = { serializedBooleanList -> serializedBooleanList.rawValue.toList().map { e -> e.rawValue } }

    open class Builder @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: List<Boolean>) : ListSetting.Builder<Boolean, BooleanType>(settingLocation, comment, defaultValue) {

        override fun build(): BooleanListSetting = BooleanListSetting(this.settingLocation, this.comment, this.defaultValue)

    }

}