package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class DoubleListSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: MutableList<Double>, value: MutableList<Double> = defaultValue) : ListSetting<Double>(settingIdentifier, comment, defaultValue, value) {

    class Builder(id: SettingIdentifier, defaultValue: MutableList<Double>) : ListSetting.Builder<Double>(id, defaultValue) {

        override fun build(): DoubleListSetting {
            return DoubleListSetting(this.id, this.comment, this.defaultValue)
        }

    }

}