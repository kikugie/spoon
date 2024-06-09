package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class BooleanListSetting(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: List<Boolean>,
    value: List<Boolean> = defaultValue) : ListSetting<Boolean, BooleanType>(settingIdentifier, comment, defaultValue, value) {

    override val valueAsSerialized: ListType<BooleanType>
        get() = ListType(this.value.toList().map { e -> BooleanType(e) })

    override val defaultValueAsSerialized: ListType<BooleanType>
        get() = ListType(this.defaultValue.toList().map { e -> BooleanType(e) })

    override fun setFromSerializedValue(serializedValue: ListType<BooleanType>) {
        this.value = serializedValue.value.toList().map { e -> e.value }
    }

    class Builder(id: SettingIdentifier, comment: String? = null, defaultValue: List<Boolean>) : ListSetting.Builder<Boolean, BooleanType>(id, comment, defaultValue) {

        override fun build(): BooleanListSetting {
            return BooleanListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}