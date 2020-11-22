package leo21.evaluated

fun Evaluated.getOrNull(name: String): Evaluated? =
	rhsOrNull?.accessOrNull(name)