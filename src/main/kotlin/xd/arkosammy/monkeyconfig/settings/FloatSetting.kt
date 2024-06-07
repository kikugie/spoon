package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.FloatArgumentType
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class FloatSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: Float, value: Float = defaultValue, private val lowerBound: Float? = null, private val upperBound: Float? = null) : ConfigSetting<Float>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<Float, FloatArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Float>
        get() = Float::class.java

    override val argumentType : FloatArgumentType
        get() {
            if (lowerBound != null) {
                return if(upperBound != null) {
                    FloatArgumentType.floatArg(lowerBound, upperBound)
                } else {
                    FloatArgumentType.floatArg(lowerBound)
                }
            }
            return FloatArgumentType.floatArg()
        }

    override var value: Float
        get() = super.value
        set(value) {
            if (lowerBound != null && value < this.lowerBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.settingIdentifier} is below the lower bound!")
                return
            }

            if (this.upperBound != null && value > this.upperBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.settingIdentifier} is above the upper bound!")
                return
            }
            super.value = value
        }

    class Builder(id: SettingIdentifier, defaultValue: Float) : ConfigSetting.Builder<Float, FloatSetting>(id, defaultValue) {

        private var lowerBound: Float? = null
        private var upperBound: Float? = null

        fun withLowerBound(lowerBound: Float) : Builder {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: Float) : Builder {
            this.upperBound = upperBound
            return this
        }

        override fun build(): FloatSetting {
            return FloatSetting(id, this.comment, defaultValue, defaultValue, lowerBound, upperBound)
        }

    }

}