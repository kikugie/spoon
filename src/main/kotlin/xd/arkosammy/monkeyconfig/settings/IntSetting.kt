package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.IntegerArgumentType
import xd.arkosammy.monkeyconfig.managers.ConfigManager
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

class IntSetting(configManager: ConfigManager, settingIdentifier: SettingIdentifier, comment: String?, value: Int, private val lowerBound: Int? = null, private val upperBound: Int? = null) : ConfigSetting<Int>(configManager, settingIdentifier, comment, value), CommandControllableSetting<Int, IntegerArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor<Int, IntegerArgumentType>) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Int>
        get() = Int::class.java

    override val argumentType : IntegerArgumentType
        get() {
            if (lowerBound != null) {
                return if(upperBound != null) {
                    IntegerArgumentType.integer(lowerBound, upperBound)
                } else {
                    IntegerArgumentType.integer(lowerBound)
                }
            }
            return IntegerArgumentType.integer()
        }

    override var value: Int
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

        override fun build(configManager: ConfigManager): IntSetting {
            return IntSetting(configManager, id, this.comment, defaultValue, lowerBound, upperBound)
        }

    }

}