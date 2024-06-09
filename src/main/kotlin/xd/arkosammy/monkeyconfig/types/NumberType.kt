package xd.arkosammy.monkeyconfig.types

@JvmInline
value class NumberType<out T : Number>(override val value: T) : SerializableType<T> {

}