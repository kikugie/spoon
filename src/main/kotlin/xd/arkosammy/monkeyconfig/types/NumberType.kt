package xd.arkosammy.monkeyconfig.types

@JvmInline
value class NumberType<T : Number>(override val rawValue: T) : SerializableType<T>