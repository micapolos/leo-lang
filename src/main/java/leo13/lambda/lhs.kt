package leo13.lambda

data class Lhs<out T>(val value: T)

fun <T> lhs(value: T) = Lhs(value)