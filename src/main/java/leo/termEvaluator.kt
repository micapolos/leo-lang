package leo

import leo.base.Stream
import leo.base.fold
import leo.base.orNull
import leo.binary.Bit

data class TermEvaluator(
	val fieldEvaluator: Evaluator<Field<Nothing>>)

val emptyTermEvaluator =
	TermEvaluator(emptyFieldEvaluator.evaluator)

fun TermEvaluator.evaluate(term: Term<Nothing>): TermEvaluator? =
	orNull.fold(term.fieldStreamOrNull) { field ->
		this?.fieldEvaluator?.evaluate(field)?.let { TermEvaluator(it) }
	}

fun TermEvaluator.evaluateInternal(term: Term<Nothing>): Evaluator<Term<Nothing>>? =
	evaluate(term)?.evaluator

fun TermEvaluator.apply(term: Term<Nothing>): Match? =
	fieldEvaluator.apply(term)

val TermEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		fieldEvaluator.bitStreamOrNull

val TermEvaluator.evaluator: Evaluator<Term<Nothing>>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
