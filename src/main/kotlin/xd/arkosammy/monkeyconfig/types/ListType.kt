package xd.arkosammy.monkeyconfig.types
import com.electronwill.nightconfig.core.file.FileConfig

@JvmInline
value class ListType<E : SerializableType<*>>(override val rawValue: List<E>) : SerializableType<List<E>> {

    /**
     * Represents the [List] stored in this [ListType] instance where each element has been mapped to its actual type.
     * This should be used when writing a [ListType] instance to a [FileConfig]
     */
    val rawList: List<*>
        get() = this.rawValue.toList().map { e -> e.rawValue }
}
