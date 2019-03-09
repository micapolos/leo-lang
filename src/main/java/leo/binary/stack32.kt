package leo.binary

import leo.base.udecOrNull
import leo.base.uincOrNull

data class Stack32<out T>(
	val array32: Array32<T>,
	val topIndex: Int?)

data class PoppedStack32<out T>(
	val value: T,
	val stack32: Stack32<T>)

val <T> T.emptyStack32: Stack32<T>
	get() =
		Stack32(array32, null)

val <T> Stack32<T>.isEmpty
	get() =
		topIndex == null

val <T> Stack32<T>.empty: Stack32<T>
	get() =
		copy(topIndex = null)

val <T> Stack32<T>.grow: Stack32<T>?
	get() =
		if (topIndex == null) copy(topIndex = 0)
		else topIndex.uincOrNull?.let { Stack32(array32, it) }

val <T> Stack32<T>.shrink: Stack32<T>?
	get() =
		if (topIndex == null) null
		else topIndex.udecOrNull?.let { Stack32(array32, it) } ?: empty

val <T : Any> Stack32<T>.top: T?
	get() =
		if (topIndex == null) null
		else array32.at(topIndex)

fun <T> Stack32<T>.putTop(value: T): Stack32<T>? =
	if (topIndex == null) null
	else copy(array32.put(topIndex, value))

fun <T> Stack32<T>.push(value: T): Stack32<T>? =
	grow?.putTop(value)

fun <T : Any> Stack32<T>.popped(value: T): PoppedStack32<T> =
	PoppedStack32(value, this)

val <T : Any> Stack32<T>.pop: PoppedStack32<T>?
	get() =
		if (topIndex == null) null
		else (topIndex.udecOrNull?.let { Stack32(array32, it) } ?: empty).popped(array32.at(topIndex))
