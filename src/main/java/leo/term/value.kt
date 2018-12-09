package leo.term

data class Value<out V>(
	val term: Term<V>)

val <V> Term<V>.value: Value<V>
	get() =
		Value(this)

fun <V, R> Value<V>.map(fn: V.() -> R): Value<R> =
	term.map(fn).value