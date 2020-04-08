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

fun <V : Any> V.orNullIf(fn: V.() -> Boolean): V? =
	if (fn()) null else this

fun <V: Any> V?.orIfNull(fn: () -> V) =
	this ?: fn()

fun <R, V : Any> R.updateIfNotNull(valueOrNull: V?, fn: R.(V) -> R): R =
	if (valueOrNull != null) fn(valueOrNull)
	else this

fun <V> V.updateIf(boolean: Boolean, fn: V.() -> V): V =
	if (boolean) fn()
	else this

fun <R, V : Any> R.updateOrNullIfNotNull(valueOrNull: V?, fn: R.(V) -> R?): R? =
	if (valueOrNull != null) fn(valueOrNull)
	else this

fun <V : Any, R> V?.ifNotNull(fn: (V) -> R?): R? =
	if (this != null) fn(this) else null

fun <V : Any> V?.notNullAnd(fn: (V) -> Boolean): Boolean =
	if (this != null) fn(this) else false

inline fun <V : Any> notNullIf(condition: Boolean, fn: () -> V): V? =
	if (condition) fn() else null

fun <V: Any> ifOrNull(condition: Boolean, fn: () -> V?): V? =
	if (condition) fn() else null

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

fun <V> failIfOr(condition: Boolean, fn: () -> V): V =
	if (condition) fail() else fn()

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
	else fn().iterate(count.dec(), fn)

fun <V> V.runIf(boolean: Boolean, fn: V.() -> V): V =
	if (boolean) fn()
	else this

fun <V : Any> V?.nullableEq(value: V?, fn: V.(V) -> Boolean): Boolean =
	if (this == null) value == null
	else value != null && fn(value)

fun <V : Any> V?.nullableContains(value: V?, fn: V.(V) -> Boolean): Boolean =
	if (this == null) value == null
	else value == null || fn(value)

fun <V : Any> V?.nullableMerge(valueOrNull: V?, union: V.(V) -> V?): The<V?>? =
	if (this == null) the(valueOrNull)
	else if (valueOrNull == null) the(this)
	else union(valueOrNull)?.the

fun <V : Any> V?.notNullOrError(message: String): V =
	this ?: error(message)

val Any?.print get() = print(this)
val Any?.println get() = println(this)

inline fun <R : Any> R.whileNotNull(fn: R.() -> R?): R {
	var value: R = this
	do {
		val newValue = value.fn() ?: return value
		value = newValue
	} while (true)
}

