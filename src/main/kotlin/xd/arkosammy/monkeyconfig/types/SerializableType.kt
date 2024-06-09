package xd.arkosammy.monkeyconfig.types

sealed interface SerializableType<out T : Any> {

    val value: T

}
