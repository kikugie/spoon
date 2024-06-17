package xd.arkosammy.monkeyconfig.types

@JvmInline
value class EnumType<T : Enum<*>>(override val rawValue: T) : SerializableType<T>