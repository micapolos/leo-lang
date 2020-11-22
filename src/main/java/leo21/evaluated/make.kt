package leo21.evaluated

fun Evaluated.make(name: String): Evaluated =
	evaluated(name lineTo this)
