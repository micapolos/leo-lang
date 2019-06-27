package leo7

typealias Write<T> = (T) -> Writer<T>?

data class Writer<T>(val write: Write<T>)

fun <T> writer(write: Write<T>) = Writer(write)
