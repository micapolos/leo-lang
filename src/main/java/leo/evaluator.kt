package leo

data class Evaluator<V>(
	val evaluate: (V) -> Evaluator<V>?)
