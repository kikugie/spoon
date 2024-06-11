package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.BooleanType
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class BooleanListSetting @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: List<Boolean>,
    value: List<Boolean> = defaultValue) : ListSetting<Boolean, BooleanType>(settingIdentifier, comment, defaultValue, value) {

    override val serializedValue: ListType<BooleanType>
        get() = ListType(this.value.toList().map { e -> BooleanType(e) })

    override val serializedDefaultValue: ListType<BooleanType>
        get() = ListType(this.defaultValue.toList().map { e -> BooleanType(e) })

    override fun setValueFromSerialized(serializedValue: ListType<BooleanType>) {
        this.value = serializedValue.value.toList().map { e -> e.value }
    }

    class Builder @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: List<Boolean>) : ListSetting.Builder<Boolean, BooleanType>(id, comment, defaultValue) {

        override fun build(): BooleanListSetting {
            return BooleanListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}