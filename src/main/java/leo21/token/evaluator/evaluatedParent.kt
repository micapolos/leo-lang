package leo21.token.evaluator

import leo21.evaluator.Evaluated
import leo21.token.processor.EvaluatorTokenProcessor
import leo21.token.processor.TokenProcessor

sealed class EvaluatedParent
data class EvaluatorNameEvaluatedParent(val evaluator: TokenEvaluator, val name: String) : EvaluatedParent()
data class EvaluatorDoEvaluatedParent(val evaluator: TokenEvaluator) : EvaluatedParent()

fun EvaluatedParent.plus(evaluated: Evaluated): TokenProcessor =
	when (this) {
		is EvaluatorNameEvaluatedParent ->
			EvaluatorTokenProcessor(evaluator.plus(name, evaluated))
		is EvaluatorDoEvaluatedParent ->
			EvaluatorTokenProcessor(evaluator.plusDo(evaluated))
	}
