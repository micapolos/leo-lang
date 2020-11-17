package leo21.evaluator

data class EvaluatedGiven(val evaluated: Evaluated)

val Evaluated.given get() = EvaluatedGiven(this)