package leo21.evaluated

data class EvaluatedGiven(val evaluated: Evaluated)

val Evaluated.given get() = EvaluatedGiven(this)