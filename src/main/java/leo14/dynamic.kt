package leo14

data class Dynamic<T>(private var _currentVar: T) {
	operator fun invoke() = _currentVar
	fun <R> with(new: T, fn: () -> R): R {
		val old = this._currentVar
		this._currentVar = new
		return try {
			fn()
		} finally {
			this._currentVar = old
		}
	}
}

val <T> T.dynamic get() = Dynamic(this)
