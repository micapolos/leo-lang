package leo14.lambda

data class Abstraction<T>(val body: T)

fun <T> abstraction(body: T) = Abstraction(body)