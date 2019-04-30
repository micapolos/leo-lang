package leo.base

data class Ok<V>(val value: V)

fun <V> ok(value: V) = Ok(value)