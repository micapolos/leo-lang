package leo9

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Stack<out T>

data class EmptyStack<out T>(
	val empty: Empty) : Stack<T>()

data class LinkStack<out T>(
	val link: StackLink<T>) : Stack<T>()

data class StackLink<out T>(
	val stack: Stack<T>,
	val value: T)

// ------------

fun <T> stack(empty: Empty): Stack<T> = EmptyStack(empty)
fun <T> stack(pop: StackLink<T>): Stack<T> = LinkStack(pop)
fun <T> stack(vararg values: T): Stack<T> = stack<T>(empty).fold(values) { push(it) }
fun <T> link(tail: Stack<T>, head: T) = StackLink(tail, head)
fun <T> Stack<T>.push(value: T) = stack(link(this, value))
val <T> Stack<T>.linkOrNull get() = (this as? LinkStack)?.link
val <T> Stack<T>.link get() = linkOrNull!!
val <T> Stack<T>.pop get() = link.stack
val <T> Stack<T>.top get() = link.value
