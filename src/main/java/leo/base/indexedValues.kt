package leo.base

infix fun <T> Int.indexed(value: T) =
	IndexedValue(this, value)

infix fun <T> IndexedValue<T>.or1Shl(bitCount: Int) =
	(1.shl(bitCount) or index) indexed value