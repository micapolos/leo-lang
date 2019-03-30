package leo32.base

data class IntArray(val array: Array<Int>) : Ram<Int, IntArray> {
	override fun at(index: Int) =
		array.at(index)

	override fun update(index: Int, fn: Int.() -> Int) =
		array.update(index, fn).intArray
}

val Array<Int>.intArray
	get() =
		IntArray(this)

val intArray =
	0.array.intArray
