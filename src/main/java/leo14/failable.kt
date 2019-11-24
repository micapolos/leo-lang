package leo14

sealed class Failable<T>
data class Success<T>(val value: T) : Failable<T>()
data class Failure<T>(val throwable: Throwable) : Failable<T>()

fun <T> success(value: T): Failable<T> = Success(value)
fun <T> failure(runtimeException: RuntimeException): Failable<T> = Failure(runtimeException)

fun <T> failable(fn: () -> T): Failable<T> =
	try {
		success(fn())
	} catch (runtimeException: RuntimeException) {
		failure(runtimeException)
	}
