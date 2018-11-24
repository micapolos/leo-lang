package leo

data class Evaluate<out V>(
	val term: Term<V>)

val <V> Term<V>.evaluate: Evaluate<V>
	get() =
		Evaluate(this)
