package leo9

import leo.base.*
import leo10.list
import leo10.prepend

sealed class Stack<out T>

data class EmptyStack<out T>(
	val empty: Empty) : Stack<T>() {
	override fun toString() = "stack"
}

data class LinkStack<out T>(
	val link: StackLink<T>) : Stack<T>() {
	override fun toString() = "${link.stack}.push(${link.value})"
}

data class StackLink<out T>(
	val stack: Stack<T>,
	val value: T) {
	override fun toString() = "link($stack, $value)"
}

// ------------

fun <T> stack(empty: Empty): Stack<T> = EmptyStack(empty)
fun <T> stack(pop: StackLink<T>): Stack<T> = LinkStack(pop)
fun <T> stack(vararg values: T): Stack<T> = stack<T>(empty).fold(values) { push(it) }
fun <T> stackLink(value: T, vararg values: T) = link(stack(), value).fold(values) { push(it) }
fun <T> nonEmptyStack(value: T, vararg values: T) = stack(stackLink(value, *values))
fun <T> link(tail: Stack<T>, head: T) = StackLink(tail, head)
fun <T> Stack<T>.push(value: T) = stack(link(this, value))
fun <T> StackLink<T>.push(value: T) = link(stack(this), value)
val <T> Stack<T>.linkOrNull get() = (this as? LinkStack)?.link
val <T> Stack<T>.link get() = linkOrNull!!
val <T> Stack<T>.pop get() = link.stack
val <T> Stack<T>.top get() = link.value
val <T : Any> Stack<T>.onlyOrNull
	get() = linkOrNull?.let { link ->
		notNullIf(link.stack.isEmpty) {
			link.value
		}
	}

tailrec fun <R, T> R.fold(stack: Stack<T>, fn: R.(T) -> R): R =
	when (stack) {
		is EmptyStack -> this
		is LinkStack -> fn(stack.link.value).fold(stack.link.stack, fn)
	}

val <T> Stack<T>.reverse get() = stack<T>().fold(this) { push(it) }
val <T> Stack<T>.list get() = list<T>().fold(this) { prepend(it) }
val Stack<*>.isEmpty get() = this is EmptyStack

fun <T> Stack<T>.any(fn: T.() -> Boolean): Boolean =
	false.fold(this) { or(fn(it)) }

fun <T> Stack<T>.all(fn: T.() -> Boolean): Boolean =
	true.fold(this) { and(fn(it)) }

fun <T, R> Stack<T>.map(fn: T.() -> R): Stack<R> =
	stack<R>().fold(this) { push(fn(it)) }.reverse

tailrec fun <T, R : Any> Stack<T>.mapFirst(fn: T.() -> R?): R? =
	when (this) {
		is EmptyStack -> null
		is LinkStack -> link.value.fn() ?: link.stack.mapFirst(fn)
	}

fun <T, R> Stack<T>.flatMap(fn: T.() -> Stack<R>): Stack<R> =
	stack<R>().fold(this) { value ->
		fold(value.fn()) { mappedValue ->
			push(mappedValue)
		}
	}.reverse

fun <T, R : Any> Stack<T>.mapOrNull(fn: T.() -> R?): Stack<R>? =
	stack<R>().orNull.fold(this) { value ->
		this?.run {
			value.fn()?.let { mapped -> push(mapped) }
		}
	}?.reverse

tailrec fun <T : Any> Stack<T>.get(int: Int): T? =
	when (this) {
		is EmptyStack -> null
		is LinkStack -> if (int == 0) link.value else link.stack.get(int.dec())
	}

tailrec fun <A : Any, B : Any, R> R.zipFold(stackA: Stack<A>, stackB: Stack<B>, fn: R.(A?, B?) -> R): R =
	when (stackA) {
		is EmptyStack ->
			when (stackB) {
				is EmptyStack -> this
				is LinkStack -> fn(null, stackB.link.value).zipFold(stackA, stackB.link.stack, fn)
			}
		is LinkStack ->
			when (stackB) {
				is EmptyStack -> fn(stackA.link.value, null).zipFold(stackA.link.stack, stackB, fn)
				is LinkStack -> fn(stackA.link.value, stackB.link.value).zipFold(stackA.link.stack, stackB.link.stack, fn)
			}
	}

fun <A : Any, B : Any> zip(stackA: Stack<A>, stackB: Stack<B>): Stack<Pair<A?, B?>> =
	stack<Pair<A?, B?>>().zipFold(stackA, stackB) { a, b -> push(a to b) }.reverse

val <T> Stack<T>.indexed
	get() =
	stack<IndexedValue<T>>().fold(this) {
		push((linkOrNull?.value?.index?.inc() ?: 0) indexed it)
	}.reverse
