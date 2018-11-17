package leo.base

data class Writer<in V>(
	val writeFn: V.() -> Writer<V>)

fun <V> Writer<V>.write(value: V): Writer<V> =
	value.writeFn()

fun <V> Writer<V>.write(stream: Stream<V>) =
	fold(stream, Writer<V>::write)
