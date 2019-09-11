package leo13

data class Mismatch<out E, out A>(val expected: Expected<E>, val actual: Actual<A>)

fun <E, A> mismatch(expected: Expected<E>, actual: Actual<A>) = Mismatch(expected, actual)
