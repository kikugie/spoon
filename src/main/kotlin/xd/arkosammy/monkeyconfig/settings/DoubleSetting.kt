package xd.arkosammy.monkeyconfig.settings

import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class DoubleSetting(name:String, comment: String?, value: Double, private val lowerBound: Double? = null, private val upperBound: Double? = null) : ConfigSetting<Double>(name, comment, value) {

    override var value: Double
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

    class Builder(id: SettingIdentifier, defaultValue: Double) : ConfigSetting.Builder<Double, DoubleSetting>(id, defaultValue) {

        private var lowerBound: Double? = null
        private var upperBound: Double? = null

        fun withLowerBound(lowerBound: Double) : Builder {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: Double) : Builder {
            this.upperBound = upperBound
            return this
        }

        override fun build(): DoubleSetting {
            return DoubleSetting(id.settingName, this.comment, defaultValue, lowerBound, upperBound)
        }

    }

}