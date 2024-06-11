package xd.arkosammy.monkeyconfig.settings.list

import xd.arkosammy.monkeyconfig.settings.ListSetting
import xd.arkosammy.monkeyconfig.types.ListType
import xd.arkosammy.monkeyconfig.types.StringType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class StringListSetting @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: List<String>,
    value: List<String> = defaultValue) : ListSetting<String, StringType>(settingIdentifier, comment, defaultValue, value) {

    override val serializedValue: ListType<StringType>
        get() = ListType(this.value.toList().map { e -> StringType(e) })

    override val serializedDefaultValue: ListType<StringType>
        get() = ListType(this.defaultValue.toList().map { e -> StringType(e) })

    override fun setValueFromSerialized(serializedValue: ListType<StringType>) {
        this.value = serializedValue.value.toList().map { e -> e.value }
    }

    class Builder @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: List<String>) : ListSetting.Builder<String, StringType>(id, comment, defaultValue) {

        override fun build(): StringListSetting {
            return StringListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}