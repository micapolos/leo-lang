package leo

import leo.base.Bit
import leo.base.Stream

data class Evaluator<V>(
	val evaluate: (V) -> Evaluator<V>?,
	val apply: (Term<Nothing>) -> Term<Nothing>?,
	val bitStreamOrNull: () -> Stream<Bit>?)
