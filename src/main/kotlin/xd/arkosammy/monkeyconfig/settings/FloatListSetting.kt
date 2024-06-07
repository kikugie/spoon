package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class FloatListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Float>, value: MutableList<Float> = defaultValue) : ListSetting<Float>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Float>) : ListSetting.Builder<Float>(id, defaultValue) {

        override fun build(): FloatListSetting {
            return FloatListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}