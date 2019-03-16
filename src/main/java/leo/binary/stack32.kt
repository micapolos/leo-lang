package leo.binary

import leo.base.ifNotNull
import leo.base.indexed
import leo.base.udecOrNull
import leo.base.uincOrNull

data class Stack32<T>(
	val map32: Arr32<T?>,
	val topIndex: Int?)

data class PoppedStack32<T>(
	val value: T,
	val stack32: Stack32<T>)

fun <T> emptyStack32(): Stack32<T> =
	Stack32((null as T?).arr32, null)

val <T> Stack32<T>.isEmpty
	get() =
		topIndex == null

val <T> Stack32<T>.empty: Stack32<T>
	get() =
		copy(topIndex = null)

val <T> Stack32<T>.grow: Stack32<T>?
	get() =
		if (topIndex == null) copy(topIndex = 0)
		else topIndex.uincOrNull?.let { Stack32(map32, it) }

val <T> Stack32<T>.shrink: Stack32<T>?
	get() =
		pop?.stack32

val <T> Stack32<T>.top: T?
	get() =
		if (topIndex == null) null
		else map32.at(topIndex)

fun <T> Stack32<T>.putTop(value: T): Stack32<T>? =
	if (topIndex == null) null
	else copy(map32.put(topIndex, value))

fun <T> Stack32<T>.push(value: T): Stack32<T>? =
	grow?.putTop(value)

fun <T> Stack32<T>.popped(value: T): PoppedStack32<T> =
	PoppedStack32(value, this)

val <T> Stack32<T>.pop: PoppedStack32<T>?
	get() =
		if (topIndex == null) null
		else (topIndex.udecOrNull?.let { Stack32(map32.put(topIndex, null), it) } ?: empty).popped(map32.at(topIndex)!!)

fun <T> Stack32<T>.updateTop(fn: T.() -> T): Stack32<T>? =
	topIndex?.let {
		copy(map32 = map32.update(it) { fn(this!!) })
	}

fun <T, R> R.foldIndexed(stack: Stack32<T>, fn: R.(IndexedValue<T>) -> R): R =
	ifNotNull(stack.topIndex) { topIndex ->
		(0..topIndex).fold(this) { folded, index ->
			folded.fn(index indexed stack.map32.at(index)!!)
		}
	}