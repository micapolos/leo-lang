package leo14.lambda

data class Application<out T>(val lhs: T, val rhs: T) {
	override fun toString() = "$lhs($rhs)"
}

fun <T> application(lhs: T, rhs: T) = Application(lhs, rhs)
