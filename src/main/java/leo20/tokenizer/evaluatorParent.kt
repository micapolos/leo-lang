package leo20.tokenizer

import leo20.lineTo

sealed class EvaluatorParent
data class ResolveEvaluatorParent(val name: String, val evaluator: Evaluator) : EvaluatorParent()

fun EvaluatorParent.end(evaluated: Evaluated): Tokenizer =
	when (this) {
		is ResolveEvaluatorParent -> EvaluatorTokenizer(evaluator.push(name lineTo evaluated.value))
	}