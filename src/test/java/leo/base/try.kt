package leo.base

sealed class Try<out V>
data class OkTry<V>(val ok: Ok<V>) : Try<V>()
data class ThrowableTry<V>(val throwable: Throwable) : Try<V>()

fun <V> aTry(ok: Ok<V>) = OkTry(ok) as Try<V>
fun <V> aTry(throwable: Throwable) = ThrowableTry<V>(throwable)

fun <V> tryDo(fn: () -> V): Try<V> {
	val value: V
	try {
		value = fn()
	} catch (throwable: Throwable) {
		return aTry(throwable)
	}
	return aTry(ok(value))
}

fun <V, R> V.tryRun(fn: V.() -> R): Try<R> =
	tryDo { this.fn() }

val Try<*>.throwableOrNull
	get() =
		(this as ThrowableTry).throwable
