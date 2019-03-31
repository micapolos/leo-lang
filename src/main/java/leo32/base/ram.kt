package leo32.base

interface Ram<T> {
	fun at(index: Int): T
	fun update(index: Int, fn: T.() -> T): Ram<T>
	fun put(index: Int, value: T) = update(index) { value }
}
