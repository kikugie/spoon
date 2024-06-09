package xd.arkosammy.monkeyconfig.types

@JvmInline
value class EnumType<T : Enum<*>>(override val value: T) : SerializableType<T>