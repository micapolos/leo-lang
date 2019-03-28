package leo32.base

import leo.base.appendableString
import leo.base.fold

data class Stack<T>(
	val array: Array<T>,
	val bottomIndex: Int,
	val topIndex: Int) {
	override fun toString() = appendableString {
		it
			.append("${array.default}.stack")
			.fold(bottomIndex..topIndex) { index ->
				append(".push(${at(index)})")
			}
	}
}

val <T> T.stack
	get() =
		Stack(array, 0, -1)

fun <T : Any> T.stackOf(vararg values: T) =
	stack.fold(values) { push(it) }

fun <T : Any> Stack<T>.pushAll(iterable: Iterable<T>) =
	fold(iterable) { push(it) }

fun <T> Stack<T>.at(index: Int) =
	array.at(index)

fun <T> Stack<T>.put(index: Int, value: T): Stack<T> =
	copy(array = array.put(index, value))

fun <T> Stack<T>.update(index: Int, fn: T.() -> T): Stack<T> =
	copy(array = array.update(index, fn))

val <T : Any> Stack<T>.top: T
	get() =
		at(topIndex)

fun <T : Any> Stack<T>.push(value: T) =
	topIndex.inc().let { newTop ->
		copy(
			array = array.put(newTop, value),
			topIndex = newTop)
	}

fun <T : Any> Stack<T>.pop(): Pair<Stack<T>, T> =
	copy(
		array = array.put(topIndex, array.default),
		topIndex = topIndex.dec()) to top

val <T : Any> Stack<T>.drop
	get() =
		pop().first

fun <T : Any> Stack<T>.op1(fn: T.() -> T) =
	update(topIndex, fn)

fun <T : Any> Stack<T>.op2(fn: T.(T) -> T) =
	topIndex.dec().let { newTop ->
		copy(
			array = array.put(newTop, at(newTop).fn(at(topIndex))).put(topIndex, array.default),
			topIndex = newTop)
	}

fun <T : Any> Stack<T>.load(index: Int) =
	push(at(index))

fun <T : Any> Stack<T>.store(index: Int) =
	copy(
		array = array.put(index, array.at(topIndex)),
		topIndex = topIndex.dec())

fun <T : Any> Stack<T>.loadFromTop(offset: Int) =
	load(topIndex - offset)

fun <T : Any> Stack<T>.storeFromTop(offset: Int) =
	store(topIndex - offset)

fun <T, R> R.fold(stack: Stack<T>, fn: R.(T) -> R): R =
	fold(stack.bottomIndex..stack.topIndex) {
		fn(stack.at(it))
	}