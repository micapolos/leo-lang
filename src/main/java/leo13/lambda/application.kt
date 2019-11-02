package leo13.lambda

data class Application<out T>(val lhs: T, val rhs: T)

fun <T> application(lhs: T, rhs: T) = Application(lhs, rhs)
