package xd.arkosammy.monkeyconfig.types

/**
 * A sealed interface used to define all possible types of values that can be written to and read from config files.
 */
sealed interface SerializableType<out T : Any> {

    val value: T

}

fun toSerializedType(value: Any): SerializableType<*> {
    return when (value) {
        is SerializableType<*> -> value
        is List<*> -> ListType(value.filterNotNull().map { e -> toSerializedType(e) })
        is Number -> NumberType(value)
        is String -> StringType(value)
        is Boolean -> BooleanType(value)
        is Enum<*> -> EnumType(value)
        else -> throw IllegalArgumentException("Value $value of type \"${value::class.simpleName}\" cannot be converted to an instance of SerializableType")
    }
}
