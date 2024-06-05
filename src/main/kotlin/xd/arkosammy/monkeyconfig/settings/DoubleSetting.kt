package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.DoubleArgumentType
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class DoubleSetting(configManager: ConfigManager, settingIdentifier: SettingIdentifier, comment: String?, value: Double, private val lowerBound: Double? = null, private val upperBound: Double? = null) : ConfigSetting<Double>(configManager, settingIdentifier, comment, value), CommandControllableSetting<Double, DoubleArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor<Double, DoubleArgumentType>) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Double>
        get() = Double::class.java

    override val argumentType : DoubleArgumentType
        get() {
            if (lowerBound != null) {
                return if(upperBound != null) {
                    DoubleArgumentType.doubleArg(lowerBound, upperBound)
                } else {
                    DoubleArgumentType.doubleArg(lowerBound)
                }
            }
            return DoubleArgumentType.doubleArg()
        }

    override var value: Double
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

        override fun build(configManager: ConfigManager): DoubleSetting {
            return DoubleSetting(configManager, id, this.comment, defaultValue, lowerBound, upperBound)
        }

    }

}