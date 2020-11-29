package leo13

import leo.base.notNullIf

data class Link<T, H>(val tail: T, val head: H)

infix fun <T, H> T.linkTo(head: H) = Link(this, head)
val <T, H : Any> Link<T, H>.onlyHeadOrNull get() = notNullIf(tail == null) { head }

fun <T, H> Stack<H>.linkOrNull(fn: Stack<H>.() -> T): Link<T, H>? =
	linkOrNull?.run { link(fn) }

fun <T, H> StackLink<H>.link(fn: Stack<H>.() -> T): Link<T, H> =
	stack.fn() linkTo value

fun <T, H, R> Link<T, H>.combine(fn: T.(H) -> R): R = tail.fn(head)