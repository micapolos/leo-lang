package leo13.lambda

data class Rhs<out T>(val value: T)

fun <T> rhs(value: T) = Rhs(value)
