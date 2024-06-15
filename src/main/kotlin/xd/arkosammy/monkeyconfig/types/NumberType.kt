package xd.arkosammy.monkeyconfig.types

@JvmInline
value class NumberType<out T : Number>(override val rawValue: T) : SerializableType<T>