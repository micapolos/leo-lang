package leo

import leo.base.Bit
import leo.base.Stream

data class Evaluator<V>(
	val evaluateFn: (V) -> Evaluator<V>?,
	val applyFn: (Term<Nothing>) -> Match?,
	val bitStreamOrNullFn: () -> Stream<Bit>?)

fun <V> Evaluator<V>.evaluate(value: V): Evaluator<V>? =
	evaluateFn(value)

fun <V> Evaluator<V>.apply(term: Term<Nothing>): Match? =
	applyFn(term)

val <V> Evaluator<V>.bitStreamOrNull: Stream<Bit>?
	get() =
		bitStreamOrNullFn()