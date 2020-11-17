package leo21.token.evaluator

import leo21.token.processor.TokenProcessor
import leo21.token.processor.tokenProcessor

sealed class EvaluatorParent
data class EvaluatorNodeBeginEvaluatorParent(val evaluatorNodeBegin: EvaluatorNodeBegin) : EvaluatorParent()
data class EvaluatorNodeDoEvaluatorParent(val evaluatorNodeDo: EvaluatorNodeDo) : EvaluatorParent()

fun EvaluatorParent.end(evaluator: Evaluator): TokenProcessor =
	when (this) {
		is EvaluatorNodeBeginEvaluatorParent -> evaluatorNodeBegin.end(evaluator).tokenProcessor
		is EvaluatorNodeDoEvaluatorParent -> evaluatorNodeDo.end(evaluator).tokenProcessor
	}

