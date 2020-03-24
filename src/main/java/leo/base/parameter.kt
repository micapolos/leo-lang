package leo.base

data class Parameter<T>(var value: T)

fun <T> parameter(value: T) = Parameter(value)

fun <T> Parameter<T>.update(fn: (T) -> T) {
	value = fn(value)
}

fun <T, R> Parameter<T>.run(newValue: T, fn: () -> R): R =
	newValue.let { oldValue ->
		try {
			value = newValue
			fn()
		} finally {
			value = oldValue
		}
	}