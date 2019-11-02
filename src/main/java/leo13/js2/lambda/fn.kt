package leo13.js2.lambda

data class Fn<T>(val body: T)

fun <T> fn(body: T) = Fn(body)