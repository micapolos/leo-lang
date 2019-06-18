package leo5.base

typealias Write<T> = (T) -> Unit

data class Writer<T>(val write: Write<T>)

fun <T> writer(write: Write<T>) = Writer(write)
