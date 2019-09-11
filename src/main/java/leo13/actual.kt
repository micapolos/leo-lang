package leo13

data class Actual<out V>(val value: V)

fun <V> actual(value: V) = Actual(value)
