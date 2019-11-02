package leo13.lambda

data class Fn<T>(val body: T)

fun <T> fn(body: T) = Fn(body)