package leo14.untyped

fun Program.get(name: String): Program? =
	contentsOrNull?.select(name)?.let { program(it) }