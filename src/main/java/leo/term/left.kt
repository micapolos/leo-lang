package leo.term

import leo.base.ifNotNull

data class Left<out V>(
	val termOrNull: Term<V>?)

val <V> Term<V>?.left: Left<V>
	get() =
		Left(this)

fun <V> Appendable.append(left: Left<V>): Appendable =
	ifNotNull(left.termOrNull) { term ->
		append(term).append(", ")
	}

fun <V, R> Left<V>.map(fn: V.() -> R): Left<R> =
	termOrNull?.map(fn).left