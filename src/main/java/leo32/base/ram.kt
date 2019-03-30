package leo32.base

interface Ram<T, A> {
	fun at(index: Int): T
	fun update(index: Int, fn: T.() -> T): A
	fun put(index: Int, value: T) = update(index) { value }
}