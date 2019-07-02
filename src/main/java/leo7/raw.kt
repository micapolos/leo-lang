package leo7

data class Raw<out T>(val value: T)

val <T> T.raw get() = Raw(this)
operator fun <T> Raw<T>.invoke() = value
