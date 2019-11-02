package leo13.js2.lambda

data class Rhs<out T>(val value: T)

fun <T> rhs(value: T) = Rhs(value)
