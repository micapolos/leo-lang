package leo.term

import leo.base.ifNotNull

data class Right<out V>(
	val termOrNull: Term<V>?)

val <V> Term<V>?.right: Right<V>
	get() =
		Right(this)

fun <V> Appendable.append(right: Right<V>): Appendable =
	ifNotNull(right.termOrNull) { term ->
		append(term).append(", ")
	}

fun <V, R> Right<V>.map(fn: V.() -> R): Right<R> =
	termOrNull?.map(fn).right