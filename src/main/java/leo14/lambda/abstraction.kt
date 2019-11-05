package leo14.lambda

data class Abstraction<T>(val body: T) {
	override fun toString() = "fn($body)"
}

fun <T> abstraction(body: T) = Abstraction(body)