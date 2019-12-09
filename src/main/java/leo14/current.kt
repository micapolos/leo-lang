package leo14

data class Current<T>(private var t: T) {
	operator fun invoke() = t
	fun <R> with(new: T, fn: () -> R): R {
		val old = t
		t = new
		return try {
			fn()
		} finally {
			t = old
		}
	}
}

val <T> T.current get() = Current(this)
