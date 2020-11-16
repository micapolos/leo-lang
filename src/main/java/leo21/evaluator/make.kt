package leo21.evaluator

fun Evaluated.make(name: String): Evaluated =
	evaluated(name lineTo this)
