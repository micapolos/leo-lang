package leo.base

data class Processor<S, in V>(
	val state: S,
	val processFn: S.(V) -> Processor<S, V>)

fun <S, V> Processor<S, V>.process(value: V): Processor<S, V> =
	state.processFn(value)
