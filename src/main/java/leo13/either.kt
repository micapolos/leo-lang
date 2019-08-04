package leo13

sealed class Either<out A, out B>

data class FirstEither<out A, out B>(val value: A) : Either<A, B>()
data class SecondEither<out A, out B>(val value: B) : Either<A, B>()

fun <A, B> A.firstEither(): Either<A, B> = FirstEither(this)
fun <A, B> B.secondEither(): Either<A, B> = SecondEither(this)

inline fun <A, B, R> Either<A, B>.eitherLet(firstFn: (A) -> R, secondFn: (B) -> R): R =
	when (this) {
		is FirstEither -> firstFn(value)
		is SecondEither -> secondFn(value)
	}

inline fun <A, B, R> Either<A, B>.eitherRun(firstFn: A.() -> R, secondFn: B.() -> R): R =
	eitherLet({ it.firstFn() }, { it.secondFn() })

inline fun <A, B, A2> Either<A, B>.mapFirst(fn: A.() -> A2): Either<A2, B> =
	eitherRun({ fn().firstEither() }, { secondEither() })