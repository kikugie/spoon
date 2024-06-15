package xd.arkosammy.monkeyconfig.types

@JvmInline
value class EnumType<out T : Enum<*>>(override val rawValue: T) : SerializableType<T>