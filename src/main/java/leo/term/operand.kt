package leo.term

import leo.base.ifNotNull

data class Operand<out V>(
	val term: Term<V>)

val <V> Term<V>.operand: Operand<V>
	get() =
		Operand(this)

fun <V> Appendable.append(operand: Operand<V>): Appendable =
	ifNotNull(operand.term, Appendable::append)

fun <V, R> Operand<V>.map(fn: V.() -> R): Operand<R> =
	term.map(fn).operand