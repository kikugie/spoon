package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.commands.CommandControllableSetting
import xd.arkosammy.monkeyconfig.commands.visitors.CommandVisitor
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingIdentifier
import kotlin.math.max
import kotlin.math.min

open class NumberSetting<T : Number> @JvmOverloads constructor(
    settingIdentifier: SettingIdentifier,
    comment: String? = null,
    defaultValue: T,
    value: T = defaultValue,
    private val lowerBound: T? = null,
    private val upperBound: T? = null) : ConfigSetting<T, NumberType<T>>(settingIdentifier, comment, defaultValue, value), CommandControllableSetting<T, ArgumentType<T>> {

    override var value: T
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

    override fun setValueFromSerialized(serializedValue: NumberType<T>) {
        this.value = serializedValue.value
    }

    override val serializedValue: NumberType<T>
        get() = NumberType(this.value)

    override val serializedDefaultValue: NumberType<T>
        get() = NumberType(this.defaultValue)

    override val argumentType: ArgumentType<T>
        @Suppress("UNCHECKED_CAST")
        get() {
            if (lowerBound != null) {
                if(upperBound != null) {
                    return when (this.defaultValue) {
                        is Byte -> IntegerArgumentType.integer(max(Byte.MIN_VALUE.toInt(), lowerBound.toInt()), min(Byte.MAX_VALUE.toInt(), upperBound.toInt())) as ArgumentType<T>
                        is Short -> IntegerArgumentType.integer(max(Short.MIN_VALUE.toInt(), lowerBound.toInt()), min(Short.MAX_VALUE.toInt(), upperBound.toInt())) as ArgumentType<T>
                        is Int -> IntegerArgumentType.integer(max(Int.MIN_VALUE, lowerBound.toInt()), min(Int.MAX_VALUE, upperBound.toInt())) as ArgumentType<T>
                        is Long -> LongArgumentType.longArg(max(Long.MIN_VALUE, lowerBound.toLong()), min(Long.MAX_VALUE, upperBound.toLong())) as ArgumentType<T>
                        is Float -> FloatArgumentType.floatArg(max(Float.MIN_VALUE, lowerBound.toFloat()), min(Float.MAX_VALUE, upperBound.toFloat())) as ArgumentType<T>
                        is Double -> DoubleArgumentType.doubleArg(max(Double.MIN_VALUE, lowerBound.toDouble()), min(Double.MAX_VALUE, upperBound.toDouble())) as ArgumentType<T>
                        else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
                    }
                } else {
                    return when (this.defaultValue) {
                        is Byte -> IntegerArgumentType.integer(max(Byte.MIN_VALUE.toInt(), lowerBound.toInt()), Byte.MAX_VALUE.toInt()) as ArgumentType<T>
                        is Short -> IntegerArgumentType.integer(max(Short.MIN_VALUE.toInt(), lowerBound.toInt()), Short.MAX_VALUE.toInt()) as ArgumentType<T>
                        is Int -> IntegerArgumentType.integer(max(Int.MIN_VALUE, lowerBound.toInt())) as ArgumentType<T>
                        is Long -> LongArgumentType.longArg(max(Long.MIN_VALUE, lowerBound.toLong())) as ArgumentType<T>
                        is Float -> FloatArgumentType.floatArg(max(Float.MIN_VALUE, lowerBound.toFloat())) as ArgumentType<T>
                        is Double -> DoubleArgumentType.doubleArg(max(Double.MIN_VALUE, lowerBound.toDouble())) as ArgumentType<T>
                        else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
                    }
                }
            }
            return when (this.defaultValue) {
                is Byte -> IntegerArgumentType.integer(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()) as ArgumentType<T>
                is Short -> IntegerArgumentType.integer(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()) as ArgumentType<T>
                is Int -> IntegerArgumentType.integer() as ArgumentType<T>
                is Long -> LongArgumentType.longArg() as ArgumentType<T>
                is Float -> FloatArgumentType.floatArg() as ArgumentType<T>
                is Double -> DoubleArgumentType.doubleArg() as ArgumentType<T>
                else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
            }
        }

    override val commandIdentifier: SettingIdentifier
        get() = settingIdentifier

    // TODO: Test this
    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): T {
        return when (this.defaultValue) {
            is Byte, is Short, is Int -> IntegerArgumentType.getInteger(ctx, argumentName) as T
            is Long -> LongArgumentType.getLong(ctx, argumentName) as T
            is Float -> FloatArgumentType.getFloat(ctx, argumentName) as T
            is Double -> DoubleArgumentType.getDouble(ctx, argumentName) as T
            else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
        }
    }

    override fun accept(visitor: CommandVisitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "${this::class.simpleName}{numType=${this.value::class.simpleName}, id=${this.settingIdentifier}, comment=${this.comment ?: "null"}, defaultValue=${this.defaultValue}}, value=${this.value}, serializedType=${this.serializedDefaultValue::class.simpleName}, lowerBound=${this.lowerBound ?: "null"}, upperBound=${this.upperBound ?: "null"}}"
    }

    class Builder<T : Number> @JvmOverloads constructor(id: SettingIdentifier, comment: String? = null, defaultValue: T) : ConfigSetting.Builder<T, NumberType<T>, NumberSetting<T>>(id, comment, defaultValue) {

        private var lowerBound: T? = null
        private var upperBound: T? = null

        fun withLowerBound(lowerBound: T): Builder<T> {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: T): Builder<T> {
            this.upperBound = upperBound
            return this
        }

        override fun build(): NumberSetting<T> {
            return NumberSetting(id, this.comment, defaultValue, defaultValue, lowerBound, upperBound)
        }

    }

}

operator fun Number.compareTo(other: Number) : Int {
    return when {
        this.toDouble() > other.toDouble() -> 1
        this.toDouble() < other.toDouble() -> -1
        else -> 0
    }
}
