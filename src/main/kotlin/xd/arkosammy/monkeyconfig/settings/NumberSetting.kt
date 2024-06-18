package xd.arkosammy.monkeyconfig.settings

import com.mojang.brigadier.arguments.*
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.ServerCommandSource
import xd.arkosammy.monkeyconfig.MonkeyConfig
import xd.arkosammy.monkeyconfig.types.NumberType
import xd.arkosammy.monkeyconfig.util.SettingLocation
import kotlin.jvm.Throws
import kotlin.math.max
import kotlin.math.min

open class NumberSetting<T : Number> @JvmOverloads constructor(
    settingLocation: SettingLocation,
    comment: String? = null,
    defaultValue: T,
    value: T = defaultValue,
    private val lowerBound: T? = null,
    private val upperBound: T? = null) : AbstractCommandControllableSetting<T, NumberType<T>, ArgumentType<T>>(settingLocation, comment, defaultValue, value) {

    override var value: T
        get() = super.value
        set(value) {
            if (lowerBound != null && value < this.lowerBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.settingLocation} is below the lower bound!")
                return
            }
            if (this.upperBound != null && value > this.upperBound) {
                MonkeyConfig.LOGGER.error("Value $value for setting ${this.settingLocation} is above the upper bound!")
                return
            }
            super.value = convertNumberToTypeT(value)
        }

    override val valueToSerializedConverter: (T) -> NumberType<T>
        get() = { number -> NumberType(number) }

    override val serializedToValueConverter: (NumberType<T>) -> T
        get() = { numberType -> numberType.rawValue }

    override val argumentType: ArgumentType<T>
        @Throws(IllegalArgumentException::class)
        get() = when (this.defaultValue) {
            is Byte -> getByteArgumentType()
            is Short -> getShortArgumentType()
            is Int -> getIntegerArgumentType()
            is Long -> getLongArgumentType()
            is Float -> getFloatArgumentType()
            is Double -> getDoubleArgumentType()
            else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
        }

    // The following unchecked casts are safe if the ArgumentType provided by one of Brigadier's argument types for numerical
    // arguments are subtypes of the corresponding ArgumentType<T>.
    // For example, a NumberSetting<Double> should retrieve its ArgumentType with DoubleArgumentType.doubleArg(), which returns
    // an instance of ArgumentType<Double>. In this case, the type parameter of the setting is Double, so this argument type can be safely casted to
    // ArgumentType<Double>.
    @Suppress("UNCHECKED_CAST")
    private fun getByteArgumentType(): ArgumentType<T> {
        val min = max(Byte.MIN_VALUE.toInt(), lowerBound?.toInt() ?: Byte.MIN_VALUE.toInt())
        val max = min(Byte.MAX_VALUE.toInt(), upperBound?.toInt() ?: Byte.MAX_VALUE.toInt())
        return IntegerArgumentType.integer(min, max) as ArgumentType<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getShortArgumentType(): ArgumentType<T> {
        val min = max(Short.MIN_VALUE.toInt(), lowerBound?.toInt() ?: Short.MIN_VALUE.toInt())
        val max = min(Short.MAX_VALUE.toInt(), upperBound?.toInt() ?: Short.MAX_VALUE.toInt())
        return IntegerArgumentType.integer(min, max) as ArgumentType<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getIntegerArgumentType(): ArgumentType<T> {
        val min = max(Int.MIN_VALUE, lowerBound?.toInt() ?: Int.MIN_VALUE)
        val max = min(Int.MAX_VALUE, upperBound?.toInt() ?: Int.MAX_VALUE)
        return IntegerArgumentType.integer(min, max) as ArgumentType<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getLongArgumentType(): ArgumentType<T> {
        val min = max(Long.MIN_VALUE, lowerBound?.toLong() ?: Long.MIN_VALUE)
        val max = min(Long.MAX_VALUE, upperBound?.toLong() ?: Long.MAX_VALUE)
        return LongArgumentType.longArg(min, max) as ArgumentType<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getFloatArgumentType(): ArgumentType<T> {
        val min = max(Float.MIN_VALUE, lowerBound?.toFloat() ?: Float.MIN_VALUE)
        val max = min(Float.MAX_VALUE, upperBound?.toFloat() ?: Float.MAX_VALUE)
        return FloatArgumentType.floatArg(min, max) as ArgumentType<T>
    }

    @Suppress("UNCHECKED_CAST")
    private fun getDoubleArgumentType(): ArgumentType<T> {
        val min = max(Double.MIN_VALUE, lowerBound?.toDouble() ?: Double.MIN_VALUE)
        val max = min(Double.MAX_VALUE, upperBound?.toDouble() ?: Double.MAX_VALUE)
        return DoubleArgumentType.doubleArg(min, max) as ArgumentType<T>
    }

    // The following unchecked cast is safe as long as the type of the value retrieved by the "getX"
    // method matches the type parameter of the current NumberSetting.
    // For example, given a NumberSetting<Int>, an IntegerArgumentType should have been supplied to the command setter node
    // and the value of the command argument should be retrieved with IntegerArgumentType.getInteger(), which returns an Int, which can then be safely casted to T,
    // which in this case, is Int.
    @Suppress("UNCHECKED_CAST")
    override fun getArgumentValue(ctx: CommandContext<ServerCommandSource>, argumentName: String): T {
        return when (this.defaultValue) {
            is Byte, is Short, is Int -> IntegerArgumentType.getInteger(ctx, argumentName)
            is Long -> LongArgumentType.getLong(ctx, argumentName)
            is Float -> FloatArgumentType.getFloat(ctx, argumentName)
            is Double -> DoubleArgumentType.getDouble(ctx, argumentName)
            else -> throw IllegalArgumentException("Unsupported number type: ${this.defaultValue::class.java}")
        } as T
    }

    // The following unchecked cast is safe under the assumption
    // that T is always one of the numerical data types shown in this "when"
    // expression.
    // For instance, if the default value of this setting is of type "Double", and "number" is an Int,
    // then the "Double" branch should be taken, converting the number to a Double,
    // which should then be successfully casted to T, which in this case is Double.
    @Suppress("UNCHECKED_CAST")
    private fun convertNumberToTypeT(number: Number): T =
         when (this.defaultValue) {
            is Byte -> number.toByte()
            is Short -> number.toShort()
            is Int -> number.toInt()
            is Long -> number.toLong()
            is Float -> number.toFloat()
            is Double -> number.toDouble()
            else -> throw IllegalArgumentException("Unsupported number type: ${defaultValue::class.java}")
         } as T

    override fun toString(): String =
        "${this::class.simpleName}{numType=${this.value::class.simpleName}, location=${this.settingLocation}, comment=${this.comment ?: "null"}, defaultValue=${this.defaultValue}}, value=${this.value}, serializedType=${this.serializedDefaultValue::class.simpleName}, lowerBound=${this.lowerBound ?: "null"}, upperBound=${this.upperBound ?: "null"}}"

    open class Builder<T : Number> @JvmOverloads constructor(settingLocation: SettingLocation, comment: String? = null, defaultValue: T) : ConfigSetting.Builder<NumberSetting<T>, T, NumberType<T>>(settingLocation, comment, defaultValue) {

        protected var lowerBound: T? = null
        protected var upperBound: T? = null

        fun withLowerBound(lowerBound: T): Builder<T> {
            this.lowerBound = lowerBound
            return this
        }

        fun withUpperBound(upperBound: T): Builder<T> {
            this.upperBound = upperBound
            return this
        }

        override fun build(): NumberSetting<T> = NumberSetting(settingLocation, this.comment, defaultValue, defaultValue, lowerBound, upperBound)

    }

}

operator fun Number.compareTo(other: Number) : Int {
    val thisAsDouble: Double = this.toDouble()
    val otherAsDouble: Double = other.toDouble()
    return when {
        thisAsDouble > otherAsDouble -> 1
        thisAsDouble < otherAsDouble -> -1
        else -> 0
    }
}
