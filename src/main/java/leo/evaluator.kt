package leo

import leo.base.Bit
import leo.base.Stream
import leo.base.The

data class Evaluator<V>(
	val evaluate: (V) -> Evaluator<V>?,
	val apply: (Term<Nothing>) -> The<Term<Nothing>?>?,
	val bitStreamOrNull: () -> Stream<Bit>?)
