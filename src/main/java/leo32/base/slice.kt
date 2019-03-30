package leo32.base

import leo.base.Seq
import leo.base.thenSeqNode
import leo.base.withoutFirst

class Slice<T>(
	val array: Array<T>,
	val range: IntRange)

fun <T> Array<T>.slice(range: IntRange) =
	Slice(this, range)

fun <T> Slice<T>.at(index: Int) =
	array.at(range.first + index)

fun <T> Slice<T>.put(index: Int, value: T) =
	array.put(range.first + index, value)

val <T> Slice<T>.without0: Slice<T>
	get() =
		Slice(array, range.withoutFirst)

val <T> Slice<T>.seq: Seq<T>
	get() =
		Seq {
			if (range.isEmpty()) null
			else array.at(0).thenSeqNode(without0.seq)
		}