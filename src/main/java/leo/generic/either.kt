package leo.generic

sealed class Either<out F, out S>

data class FirstEither<F, S>(val first: First<F>) : Either<F, S>()
data class SecondEither<F, S>(val second: Second<S>) : Either<F, S>()

fun <F, S> either(first: First<F>): Either<F, S> = FirstEither(first)
fun <F, S> either(second: Second<S>): Either<F, S> = SecondEither(second)

val <F : Any> Either<F, *>.firstOrNull get() = (this as? FirstEither)?.first
val <F : Any> Either<F, *>.secondOrNull get() = (this as? SecondEither)?.second

fun <F, S, R> Either<F, S>.switch(fn0: F.() -> R, fn1: S.() -> R) =
	when (this) {
		is FirstEither -> first().fn0()
		is SecondEither -> second().fn1()
	}