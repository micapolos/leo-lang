package leo

data class Defined<out V>(
	val value: V)

fun <V> defined(value: V) = Defined(value)
