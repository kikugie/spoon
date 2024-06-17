package xd.arkosammy.monkeyconfig.types

/**
 * A sealed interface used to define all possible types of values that can be written to and read from config files.
 */
sealed interface SerializableType<T : Any> {

    val rawValue: T

}

fun toSerializedType(rawValue: Any): SerializableType<*> {
    return when (rawValue) {
        is SerializableType<*> -> rawValue
        is List<*> -> ListType(rawValue.filterNotNull().map { e -> toSerializedType(e) })
        is Number -> NumberType(rawValue)
        is String -> StringType(rawValue)
        is Boolean -> BooleanType(rawValue)
        is Enum<*> -> EnumType(rawValue)
        else -> throw IllegalArgumentException("Value $rawValue of type \"${rawValue::class.simpleName}\" cannot be converted to an instance of SerializableType")
    }
}
