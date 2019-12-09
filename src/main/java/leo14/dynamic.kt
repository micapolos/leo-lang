package leo14

data class Dynamic<T>(private var _currentVar: T) {
	val current get() = _currentVar
	fun <R> set(new: T, fn: () -> R): R {
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
