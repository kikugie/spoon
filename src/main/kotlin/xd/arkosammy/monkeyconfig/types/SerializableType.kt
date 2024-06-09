package xd.arkosammy.monkeyconfig.types

/**
 * A sealed interface used to define all possible types of values that can be written to and read from config files.
 */
sealed interface SerializableType<out T : Any> {

    val value: T

}
