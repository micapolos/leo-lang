package leo13

sealed class Result<out V, out E>

data class SuccessResult<V, E>(val value: V) : Result<V, E>()
data class FailureResult<V, E>(val value: E) : Result<V, E>()

fun <V, E> successResult(value: V): Result<V, E> = SuccessResult(value)
fun <V, E> failureResult(value: E): Result<V, E> = FailureResult(value)

fun <V, E> Result<V, E>.update(fn: V.() -> V): Result<V, E> =
	when (this) {
		is SuccessResult -> successResult(value.fn())
		is FailureResult -> this
	}

fun <V, E> Result<V, E>.resultUpdate(fn: V.() -> Result<V, E>): Result<V, E> =
	when (this) {
		is SuccessResult -> value.fn()
		is FailureResult -> this
	}

val <V> Result<V, *>.unsafeValue: V
	get() =
		when (this) {
			is SuccessResult -> value
			is FailureResult -> null!!
		}