@file:Suppress("unused")

package leo32.base

import leo.base.*
import leo.binary.zero

data class List<out T: Any>(
	val array: Array<T?>,
	val size: I32)

fun <T: Any> Empty.list() =
	List(nullOf<T>().array, zero.i32)

val <T: Any> List<T>.only: T get() {
	assert(size == 1.i32)
	return at(0.i32)
}

fun <T: Any> list(vararg ts: T) =
	empty.list<T>().fold(ts) { add(it) }

fun <T: Any> List<T>.add(value: T) =
	copy(array = array.put(size.int, value), size = size.inc)

fun <T: Any> List<T>.at(index: I32): T =
	array.at(index.int)!!

fun <T: Any> List<T>.subseq(index: I32): Seq<T> =
	Seq {
		if (index < size) at(index).thenSeqNode(subseq(index.inc))
		else null
	}

val <T: Any> List<T>.seq get() =
	subseq(zero.i32)