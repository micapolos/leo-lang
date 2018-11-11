package leo.base

fun <V : Any> nullOf() =
	null as V?

val <V> V.orNull: V?
	get() =
		this

fun <V, R> V?.ifNull(fn: () -> R): R? =
	if (this == null) fn()
	else null

val fail: Nothing
	get() =
		throw IllegalStateException()

fun <V> fail(): V =
	fail

// TODO: Escape string and char
val Any?.string
	get() =
		when {
			this is String -> "\"$this\""
			this is Char -> "\'$this\'"
			else -> toString()
		}
