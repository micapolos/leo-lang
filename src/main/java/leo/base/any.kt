package leo.base

import leo.java.lang.useResourceBitStreamOrNull

fun <V : Any> nullOf(): V? = null

val <V> V.orNull: V?
	get() =
		this

fun <V, R> V?.ifNull(fn: () -> R): R? =
	if (this == null) fn()
	else null

fun <V : Any> V.orNullIf(boolean: Boolean): V? =
	if (boolean) null else this

fun <V : Any, R> V?.ifNotNull(fn: (V) -> R?): R? =
	if (this != null) fn(this) else null

fun <V : Any, R> V?.ifNotNullOr(notNullFn: (V) -> R, nullFn: () -> R): R =
	if (this != null) notNullFn(this) else nullFn()

fun <V, R> R.ifNotNull(valueOrNull: V?, fn: R.(V) -> R): R =
	if (valueOrNull == null) this
	else fn(valueOrNull)

fun <V, R, O> R.runIfNotNull(valueOrNull: V?, fn: R.(V) -> O): O? =
	if (valueOrNull == null) null
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

fun <R> Any.useSiblingResourceBitStreamOrNull(siblingName: String, fn: Stream<EnumBit>?.() -> R): R =
	this::class.java.useResourceBitStreamOrNull(siblingName, fn)

fun <V : Any, R : Any> V?.matchNull(fn: () -> R?): R? =
	if (this == null) fn() else null

tailrec fun <V> V.iterate(count: Int, fn: V.() -> V): V =
	if (count == 0) this
	else fn().iterate(count - 1, fn)

fun <V> V.runIf(boolean: Boolean, fn: V.() -> V): V =
	if (boolean) fn()
	else this

fun <V, R> V.ifThenElse(condition: Boolean, thenFn: V.() -> R, elseFn: V.() -> R): R =
	if (condition) thenFn() else elseFn()