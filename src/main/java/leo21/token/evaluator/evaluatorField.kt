package leo21.token.evaluator

data class EvaluatorField(val name: String, val evaluator: Evaluator)

infix fun String.fieldTo(evaluator: Evaluator) = EvaluatorField(this, evaluator)