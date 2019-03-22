package leo32

import leo.base.appendableString
import leo.base.fold

data class Stack<T>(
	val array: Array<T>,
	val top: Int) {
	override fun toString() = appendableString {
		it
			.append("${array.default}.stack")
			.fold(0..top) { index ->
				append(".push(${at(index)})")
			}
	}
}

val <T> T.stack
	get() =
		Stack(array, -1)

fun <T> Stack<T>.at(index: Int) =
	array.at(index)

fun <T> Stack<T>.update(index: Int, fn: T.() -> T): Stack<T> =
	copy(array = array.update(index, fn))

fun <T : Any> Stack<T>.push(value: T) =
	top.inc().let { newTop ->
		copy(
			array = array.put(newTop, value),
			top = newTop)
	}

fun <T> Stack<T>.pop() =
	copy(
		array = array.put(top, array.default),
		top = top.dec())

fun <T : Any> Stack<T>.op1(fn: T.() -> T) =
	update(top, fn)

fun <T : Any> Stack<T>.op2(fn: T.(T) -> T) =
	top.dec().let { newTop ->
		copy(
			array = array.put(newTop, at(newTop).fn(at(top))).put(top, array.default),
			top = newTop)
	}

fun <T : Any> Stack<T>.load(index: Int) =
	push(at(index))

fun <T : Any> Stack<T>.store(index: Int) =
	copy(
		array = array.put(index, array.at(top)),
		top = top.dec())

fun <T : Any> Stack<T>.loadFromTop(offset: Int) =
	load(top - offset)

fun <T : Any> Stack<T>.storeFromTop(offset: Int) =
	store(top - offset)
