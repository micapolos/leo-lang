package leo

import leo.base.Bit
import leo.base.Stream

data class Evaluator<V>(
	val evaluateFn: (V) -> Evaluator<V>?,
	val applyFn: (Term<Nothing>) -> Match?,
	val bitStreamOrNull: () -> Stream<Bit>?)
