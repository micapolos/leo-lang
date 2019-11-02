package leo13.js2.lambda

data class Arrow<out T>(val lhs: T, val rhs: T)

fun <T> arrow(lhs: T, rhs: T) = Arrow(lhs, rhs)
