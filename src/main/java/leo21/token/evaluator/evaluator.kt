package leo21.token.evaluator

import leo21.evaluator.Evaluated
import leo21.evaluator.LineEvaluated
import leo21.evaluator.emptyEvaluated
import leo21.evaluator.evaluated
import leo21.evaluator.given
import leo21.evaluator.lineTo
import leo21.evaluator.plus

data class Evaluator(
	val context: Context,
	val evaluated: Evaluated
)

val emptyEvaluator = Evaluator(emptyContext, emptyEvaluated)

val Evaluator.resolve: Evaluator
	get() =
		copy(evaluated = context.resolve(evaluated))

fun Evaluator.plus(line: LineEvaluated): Evaluator =
	copy(evaluated = evaluated.plus(line)).resolve

fun Evaluator.plus(field: EvaluatorField): Evaluator =
	plus(field.name lineTo field.evaluator.evaluated)

val Evaluator.begin
	get() =
		Evaluator(context, evaluated())

val Evaluator.beginDo
	get() =
		Evaluator(context.plus(evaluated.given), evaluated())

fun Evaluator.do_(evaluator: Evaluator): Evaluator =
	copy(evaluated = evaluator.evaluated)