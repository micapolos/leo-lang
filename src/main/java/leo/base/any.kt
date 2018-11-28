package leo.base

import leo.java.lang.useResourceBitStreamOrNull

fun <V : Any> nullOf(): V? = null

val <V> V.orNull: V?
	get() =
		this

fun <V, R> V?.ifNull(fn: () -> R): R? =
	if (this == null) fn()
	else null

fun <V, R> R.ifNotNull(valueOrNull: V?, fn: R.(V) -> R): R =
	if (valueOrNull == null) this
	else fn(valueOrNull)

fun <V : Any> V.ifNotNull(fn: V.() -> V?): V =
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
			this is Byte -> "byte $this"
			this is Short -> "short $this"
			this is Int -> "int $this"
			this is Long -> "long $this"
			this is Float -> "float $this"
			this is Double -> "double $this"
			this is String -> "\"$this\""
			this is Char -> "\'$this\'"
			else -> toString()
		}

fun <V> identity(): (V) -> V =
	{ it }

fun <R> Any.useSiblingResourceBitStreamOrNull(siblingName: String, fn: Stream<Bit>?.() -> R): R =
	this::class.java.useResourceBitStreamOrNull(siblingName, fn)

fun <V : Any, R : Any> V?.matchNull(fn: () -> R?): R? =
	if (this == null) fn() else null

tailrec fun <V> V.iterate(count: Int, fn: V.() -> V): V =
	if (count == 0) this
	else fn().iterate(count - 1, fn)
