package leo

data class Not<out V>(
	val value: V)

fun <V> not(value: V) = Not(value)