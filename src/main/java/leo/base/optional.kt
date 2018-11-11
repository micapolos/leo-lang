package leo.base

sealed class Optional<V>

data class Absent<V>(val unit: Unit) : Optional<V>() {
	override fun toString() = "absent"
}

data class Present<V>(val that: V) : Optional<V>() {
	override fun toString() = "present($that)"
}

fun <V> absent(): Optional<V> =
	Absent(Unit)

val <V> V.present: Optional<V>
	get() =
		Present(this)

val <V> V?.optional: Optional<V>
	get() =
		this?.present ?: absent()

fun <V, R> Optional<V>.apply(
	presentFn: (V) -> R,
	absentFn: () -> R
) =
	when (this) {
		is Present -> presentFn(that)
		is Absent -> absentFn()
	}

fun <V, R> Optional<V>.map(fn: (V) -> R): Optional<R> =
	when (this) {
		is Absent -> absent()
		is Present -> fn(that).present
	}

fun <V, R> Optional<V>.mapOrNull(fn: (V) -> R): R? =
	when (this) {
		is Absent -> null
		is Present -> fn(that)
	}

fun <V, R> Optional<V>.mapToOptional(fn: (V) -> Optional<R>): Optional<R> =
	when (this) {
		is Absent -> absent()
		is Present -> fn(that)
	}

fun <V> Optional<V>.or(other: V): V =
	when (this) {
		is Absent -> other
		is Present -> that
	}

fun <V, R> Optional<V>.fold(initial: R, fn: (R, V) -> R): R =
	when (this) {
		is Absent -> initial
		is Present -> fn(initial, that)
	}
