package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.LongArgumentType
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.util.SettingIdentifier

open class LongSetting(settingIdentifier: SettingIdentifier, comment: String? = null, defaultValue: Long, value: Long = defaultValue, private val lowerBound: Long? = null, private val upperBound: Long? = null) : ConfigSetting<Long>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<Long, LongArgumentType> {

    override val commandIdentifier: SettingIdentifier
        get() = this.settingIdentifier

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override val argumentClass: Class<Long>
        get() = Long::class.java

    override val argumentType : LongArgumentType
        get() {
            if (lowerBound != null) {
                return if(upperBound != null) {
                    LongArgumentType.longArg(lowerBound, upperBound)
                } else {
                    LongArgumentType.longArg(lowerBound)
                }
            }
            return LongArgumentType.longArg()
        }

    override var value: Long
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

    class Builder(id: SettingIdentifier, defaultValue: Long) : ConfigSetting.Builder<Long, LongSetting>(id, defaultValue) {

        private var lowerBound: Long? = null
        private var upperBound: Long? = null

        fun withLowerBound(lowerBound: Long) : Builder {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: Long) : Builder {
            this.upperBound = upperBound
            return this
        }

        override fun build(): LongSetting {
            return LongSetting(id, this.comment, defaultValue, defaultValue, lowerBound, upperBound)
        }

    }

}