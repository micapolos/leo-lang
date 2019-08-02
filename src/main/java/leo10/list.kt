package leo10

import leo.base.*
import leo9.list
import leo9.push
import leo9.stack

sealed class List<out T>

data class EmptyList<out T>(
	val empty: Empty) : List<T>() {
	override fun toString() = "[]"
}

data class LinkList<out T>(
	val link: ListLink<T>) : List<T>() {
	override fun toString() = "[$link]"
}

data class ListLink<out T>(
	val head: ListHead<T>,
	val tail: ListTail<T>) {
	override fun toString() = "${head.value}${tail.list.linkOrNull.ifNotNull { ", $it" } ?: ""}"
}

data class ListHead<out T>(val value: T)
data class ListTail<out T>(val list: List<T>)

// ------------

fun <T> list(empty: Empty): List<T> = EmptyList(empty)
fun <T> list(pop: ListLink<T>): List<T> = LinkList(pop)
fun <T> list(vararg values: T): List<T> = list<T>(empty).foldRight(values) { list(link(listHead(this), listTail(it))) }
fun <T> link(head: ListHead<T>, tail: ListTail<T>) = ListLink(head, tail)
fun <T> listHead(value: T) = ListHead(value)
fun <T> listTail(list: List<T>) = ListTail(list)
fun <T> List<T>.prepend(value: T) = list(link(listHead(value), listTail(this)))
fun <T> ListHead<T>.plus(tail: ListTail<T>) = list(this, tail)
val <T> List<T>.linkOrNull get() = (this as? LinkList)?.link
val <T> List<T>.link get() = linkOrNull!!
val <T> List<T>.tail get() = link.tail
val <T> List<T>.head get() = link.head
val List<*>.isEmpty get() = this is EmptyList
val List<*>.size get() = 0.fold(this) { inc() }

tailrec fun <R, T> R.fold(list: List<T>, fn: R.(T) -> R): R =
	when (list) {
		is EmptyList -> this
		is LinkList -> fn(list.link.head.value).fold(list.link.tail.list, fn)
	}

val <T> List<T>.stack get() = stack<T>().fold(this) { push(it) }
val <T> Seq<T>.list get() = stack<T>().fold(this) { push(it) }.list
