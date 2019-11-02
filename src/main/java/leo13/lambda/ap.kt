package leo13.lambda

data class Ap<out T>(val lhs: T, val rhs: T)

fun <T> ap(lhs: T, rhs: T) = Ap(lhs, rhs)
