package leo14.untyped

fun Program.get(name: String): Program? =
	bodyOrNull?.select(name)?.let { program(it) }