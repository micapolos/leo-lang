package leo21.evaluator

fun Evaluated.getOrNull(name: String): Evaluated? =
	rhsOrNull?.accessOrNull(name)