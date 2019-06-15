package leo.parser

data class Parsed<out V>(val value: V)

fun <V> parsed(value: V) = Parsed(value)
fun <V> Parsed<V>.invoke() = value
