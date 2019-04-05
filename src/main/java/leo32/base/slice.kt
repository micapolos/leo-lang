package leo32.base

import leo.base.Seq
import leo.base.intSize
import leo.base.intWithoutStart
import leo.base.then

class Slice<T>(
	val array: Array<T>,
	val range: ClosedRange<Int>) : Ram<T> {
	override fun at(index: Int) =
		array.at(range.start + index)

	override fun update(index: Int, fn: T.() -> T) =
		Slice(array = array.update(range.start + index, fn), range = range)
}

fun <T> Array<T>.slice(range: IntRange) =
	Slice(this, range)

val Slice<*>.size
	get() =
		range.intSize

val <T> Slice<T>.without0: Slice<T>
	get() =
		Slice(array, range.intWithoutStart)

val <T> Slice<T>.seq: Seq<T>
	get() =
		Seq {
			if (range.isEmpty()) null
			else array.at(0) then without0.seq
		}