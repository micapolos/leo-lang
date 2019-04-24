package leo32.writer

data class Writer<in V>(
	val fn: V.() -> Writer<V>)

fun <V> Writer<V>.write(value: V) =
	value.fn()
