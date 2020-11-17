package leo21.token.evaluator

data class EvaluatorNodeBegin(val evaluatorNode: EvaluatorNode, val name: String)

fun EvaluatorNodeBegin.end(evaluator: Evaluator): EvaluatorNode =
	evaluatorNode.end(name fieldTo evaluator)