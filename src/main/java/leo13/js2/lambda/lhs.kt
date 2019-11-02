package leo13.js2.lambda

data class Lhs<out T>(val value: T)

fun <T> lhs(value: T) = Lhs(value)