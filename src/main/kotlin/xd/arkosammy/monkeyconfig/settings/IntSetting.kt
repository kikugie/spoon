package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class IntSetting(name:String, comment: String?, value: Int, private val lowerBound: Int? = null, private val upperBound: Int? = null) : ConfigSetting<Int>(name, comment, value) {

    override var value: Int
        get() = super.value
        set(value) {
            if (lowerBound != null && value < this.lowerBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.name} is below the lower bound!")
                return
            }

            if (this.upperBound != null && value > this.upperBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.name} is above the upper bound!")
                return
            }
            super.value = value
        }

    class Builder(id: SettingIdentifier, defaultValue: Int) : ConfigSetting.Builder<Int, IntSetting>(id, defaultValue) {

        private var lowerBound: Int? = null
        private var upperBound: Int? = null

        fun withLowerBound(lowerBound: Int): Builder {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: Int): Builder {
            this.upperBound = upperBound
            return this
        }

        override fun build(): IntSetting {
            return IntSetting(id.settingName, this.comment, defaultValue, lowerBound, upperBound)
        }

    }

}