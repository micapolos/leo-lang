package leo21.token.evaluator

data class EvaluatorNodeDo(val evaluatorNode: EvaluatorNode)

fun EvaluatorNodeDo.end(evaluator: Evaluator): EvaluatorNode =
	evaluatorNode.do_(evaluator)