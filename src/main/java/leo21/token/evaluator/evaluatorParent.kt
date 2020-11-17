package leo21.token.evaluator

import leo21.token.processor.Processor
import leo21.token.processor.processor

sealed class EvaluatorParent
data class EvaluatorNodeBeginEvaluatorParent(val evaluatorNodeBegin: EvaluatorNodeBegin) : EvaluatorParent()
data class EvaluatorNodeDoEvaluatorParent(val evaluatorNodeDo: EvaluatorNodeDo) : EvaluatorParent()

fun EvaluatorParent.end(evaluator: Evaluator): Processor =
	when (this) {
		is EvaluatorNodeBeginEvaluatorParent -> evaluatorNodeBegin.end(evaluator).processor
		is EvaluatorNodeDoEvaluatorParent -> evaluatorNodeDo.end(evaluator).processor
	}

