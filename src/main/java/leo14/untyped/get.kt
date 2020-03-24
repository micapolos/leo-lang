package leo14.untyped

fun Value.get(name: String): Thunk? =
	contentsOrNull?.select(name)?.let { thunk(value(it)) }

fun Thunk.get(name: String): Thunk? =
	value.get(name)
