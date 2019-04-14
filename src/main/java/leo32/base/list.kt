@file:Suppress("unused")

package leo32.base

import leo.base.*

data class List<out T: Any>(
	val array: Array<T?>,
	val startInclusive: I32,
	val endExclusive: I32) {
	override fun equals(other: Any?): Boolean =
		other is List<*>
			&& size == other.size
			&& true.fold(0.until(size.int).asIterable()) { this && at(it.i32) == other.at(it.i32) }
}

fun <T: Any> Empty.list() =
	List(nullOf<T>().array, 0.i32, 0.i32)

val <T: Any> List<T>.only: T get() =
	onlyOrNull!!

val <T: Any> List<T>.onlyOrNull: T? get() =
	if (size != 1.i32) null
	else at(0.i32)

fun <T: Any> list(vararg ts: T) =
	empty.list<T>().fold(ts) { add(it) }

val List<*>.size get() =
	endExclusive - startInclusive

val List<*>.isEmpty get() =
	size.isZero

fun <T: Any> List<T>.add(value: T) =
	copy(array = array.put(endExclusive.int, value), endExclusive = endExclusive.inc)

val <T: Any> List<T>.drop get() =
	copy(array = array.put(endExclusive.dec.int, null), endExclusive = endExclusive.dec)

val <T: Any> List<T>.dropFirst get() =
	copy(array = array.put(startInclusive.int, null), startInclusive = startInclusive.inc)

fun <T: Any> List<T>.at(index: I32): T =
	array.at(startInclusive.plus(index).int)!!

val <T: Any> List<T>.first: T get() = at(0.i32)

val <T : Any> List<T>.last: T get() = at(size.dec)

fun <T: Any> List<T>.subseq(index: I32): Seq<T> =
	Seq {
		notNullIf(startInclusive.plus(index) < endExclusive) {
			at(index) then subseq(index.inc)
		}
	}

val <T: Any> List<T>.seq get() =
	subseq(0.i32)
