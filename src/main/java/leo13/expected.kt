package leo13

data class Expected<out V>(val value: V)

fun <V> expected(value: V) = Expected(value)
