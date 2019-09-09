package leo13

sealed class Failable<out V>

data class SuccessFailable<V>(val value: V) : Failable<V>()
data class FailureFailable<V>(val sentence: Sentence) : Failable<V>()

fun <V> success(value: V): Failable<V> = SuccessFailable(value)
fun <V> failure(sentence: Sentence): Failable<V> = FailureFailable(sentence)
fun <V> failure(line: SentenceLine): Failable<V> = failure(sentence(line))
fun <V> failure(word: Word): Failable<V> = failure(sentence(word))

fun <V, R> Failable<V>.map(fn: V.() -> R): Failable<R> =
	when (this) {
		is SuccessFailable -> success(value.fn())
		is FailureFailable -> failure(sentence)
	}

fun <V, R> Failable<V>.failableMap(fn: V.() -> Failable<R>): Failable<R> =
	when (this) {
		is SuccessFailable -> value.fn()
		is FailureFailable -> failure(sentence)
	}

fun <V, R> Failable<V>.map(line: SentenceLine, fn: V.() -> R): Failable<R> =
	when (this) {
		is SuccessFailable -> success(value.fn())
		is FailureFailable -> failure(sentence.plus(line))
	}

fun <V, R> Failable<V>.map(word: Word, fn: V.() -> R): Failable<R> =
	when (this) {
		is SuccessFailable -> success(value.fn())
		is FailureFailable -> failure(sentence(word lineTo sentence))
	}

fun <V, R> Failable<V>.failableMap(line: SentenceLine, fn: V.() -> Failable<R>): Failable<R> =
	when (this) {
		is SuccessFailable -> value.fn()
		is FailureFailable -> failure(sentence.plus(line))
	}

fun <V, R> Failable<V>.failableMap(word: Word, fn: V.() -> Failable<R>): Failable<R> =
	when (this) {
		is SuccessFailable -> value.fn()
		is FailureFailable -> failure(sentence(word lineTo sentence))
	}

val <V : Any> Failable<V>.orNull: V?
	get() =
		when (this) {
			is SuccessFailable -> value
			is FailureFailable -> null
		}