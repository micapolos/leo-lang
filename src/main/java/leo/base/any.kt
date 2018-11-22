package leo.base

import leo.java.lang.useResourceBitStream

fun <V : Any> nullOf() =
	null as V?

val <V> V.orNull: V?
	get() =
		this

fun <V, R> V?.ifNull(fn: () -> R): R? =
	if (this == null) fn()
	else null

fun <V, R> R.ifNotNull(valueOrNull: V?, fn: R.(V) -> R): R =
	if (valueOrNull == null) this
	else fn(valueOrNull)

fun <V> V.ifNotNull(fn: V.() -> V?): V =
	fn() ?: this

val fail: Nothing
	get() =
		throw IllegalStateException()

fun <V> fail(): V =
	fail

// TODO: Escape string and char
val Any?.string
	get() =
		when {
			this is Byte -> "Byte($this)"
			this is Short -> "Short($this)"
			this is Int -> "Integer($this)"
			this is Long -> "Long($this)"
			this is Float -> "Float($this)"
			this is Double -> "Double($this)"
			this is String -> "\"$this\""
			this is Char -> "\'$this\'"
			else -> toString()
		}

fun <V> identity(): (V) -> V =
	{ it }

fun <R> Any.useSiblingResourceBitStream(siblingName: String, fn: Stream<Bit>?.() -> R): R =
	this::class.java.useResourceBitStream(siblingName, fn)
