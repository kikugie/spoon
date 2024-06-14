package xd.arkosammy.monkeyconfig.types

@JvmInline
value class EnumType<out T : Enum<*>>(override val value: T) : SerializableType<T>