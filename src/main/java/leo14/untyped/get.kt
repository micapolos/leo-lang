package leo14.untyped

fun Value.get(name: String): Value? =
	contentsOrNull?.select(name)?.let { value(it) }