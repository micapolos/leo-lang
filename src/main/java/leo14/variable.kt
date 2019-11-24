package leo14

data class Variable<T>(var current: T)

fun <T> variable(value: T) = Variable(value)

fun <T> Variable<T>.update(fn: T.() -> T) {
	current = current.fn()
}
