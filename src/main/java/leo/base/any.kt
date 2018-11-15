package leo.base

fun <V : Any> nullOf() =
	null as V?

val <V> V.orNull: V?
	get() =
		this

fun <V, R> V?.ifNull(fn: () -> R): R? =
	if (this == null) fn()
	else null

fun <V, R> R.foldIfNotNull(valueOrNull: V?, fn: R.(V) -> R): R =
	if (valueOrNull == null) this
	else fn(valueOrNull)

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

fun <V> identity(): (V) -> V =
	{ it }