package leo13

sealed class Either<out F, out S>
data class FirstEither<F, S>(val first: F) : Either<F, S>()
data class SecondEither<F, S>(val second: S) : Either<F, S>()

fun <F, S> F.firstEither(): Either<F, S> = FirstEither(this)
fun <F, S> S.secondEither(): Either<F, S> = SecondEither(this)

fun <F, S, R> Either<F, S>.select(firstFn: (F) -> R, secondFn: (S) -> R): R =
	when (this) {
		is FirstEither -> firstFn(first)
		is SecondEither -> secondFn(second)
	}

val <F> Either<F, *>.first: F get() = (this as FirstEither).first
val <S> Either<*, S>.second: S get() = (this as SecondEither).second
