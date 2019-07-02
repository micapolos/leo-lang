package leo7

sealed class Metable<out T>

data class RawMetable<T>(val raw: Raw<T>) : Metable<T>()
data class MetaMetable<T>(val meta: Meta<T>) : Metable<T>()

val <T> Raw<T>.metable: Metable<T> get() = RawMetable(this)
val <T> Meta<T>.metable: Metable<T> get() = MetaMetable(this)
