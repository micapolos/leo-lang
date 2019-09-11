package leo13

sealed class Result<out V, out E>

data class SuccessResult<V, E>(val value: V) : Result<V, E>()
data class FailureResult<V, E>(val value: E) : Result<V, E>()

data class ResultMap<V2, E2, E1>(val result: Result<V2, E2>, val error: E1)

fun <V, E> successResult(value: V): Result<V, E> = SuccessResult(value)
fun <V, E> failureResult(value: E): Result<V, E> = FailureResult(value)
fun <V, E1, E2> resultMap(result: Result<V, E2>, error2: E1) = ResultMap(value, error1, error2)

fun <V, E, R> Result<V, E>.mapSuccess(fn: V.() -> R): Result<R, E> =
	when (this) {
		is SuccessResult -> successResult(value.fn())
		is FailureResult -> failureResult(value)
	}

fun <V, E, R> Result<V, E>.mapFailure(fn: E.() -> R): Result<V, R> =
	when (this) {
		is SuccessResult -> successResult(value)
		is FailureResult -> failureResult(value.fn())
	}

fun <V, E1, V2, E2> Result<V, E1>.resultMapSuccess(fn: V.() -> Result<V2, E2>): Result<Result<V2, E2>, E1> =
	when (this) {
		is SuccessResult -> successResult(value.fn())
		is FailureResult -> failureResult(value)
	}

fun <V2, E2, E1> Result<Result<V2, E2>, E1>.resultMapFailure(fn: E2.() -> E1): Result<V2, E1> =
	when (this) {
		is SuccessResult -> value.mapFailure(fn)
		is FailureResult -> failureResult(value)
	}

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