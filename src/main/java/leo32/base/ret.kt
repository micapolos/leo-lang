package leo32.base

data class Ret<V>(val value: V)

fun <V> ret(value: V) = Ret(value)