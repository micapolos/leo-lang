package leo.generic

sealed class Either<out F, out S>

data class FirstEither<F>(val first: First<F>) : Either<F, Nothing>()
data class SecondEither<S>(val second: Second<S>) : Either<Nothing, S>()

fun <F> either(first: First<F>): Either<F, Nothing> = FirstEither(first)
fun <S> either(second: Second<S>): Either<Nothing, S> = SecondEither(second)

val <F : Any> Either<F, *>.firstOrNull get() = (this as? FirstEither)?.first
val <F : Any> Either<F, *>.secondOrNull get() = (this as? SecondEither)?.second
