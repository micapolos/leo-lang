package leo

import leo.base.*

data class FieldEvaluator(
	val tokenEvaluator: Evaluator<Token<Nothing>>)

val emptyFieldEvaluator =
	FieldEvaluator(emptyTokenEvaluator.evaluator)

fun FieldEvaluator.evaluate(field: Field<Nothing>): FieldEvaluator? =
	orNull.fold(field.tokenStream) { token ->
		this?.tokenEvaluator?.evaluate(token)?.let { FieldEvaluator(it) }
	}

fun FieldEvaluator.evaluateInternal(field: Field<Nothing>): Evaluator<Field<Nothing>>? =
	evaluate(field)?.evaluator

fun FieldEvaluator.apply(term: Term<Nothing>): Match? =
	tokenEvaluator.apply(term)

val FieldEvaluator.bitStreamOrNull: Stream<Bit>?
  get() =
	  tokenEvaluator.bitStreamOrNull

val FieldEvaluator.evaluator: Evaluator<Field<Nothing>>
	get() =
		Evaluator(
			this::evaluateInternal,
			this::apply,
			this::bitStreamOrNull)
