package leo13

import leo.base.fold

data class List<out V>(val option: ListOption<V>, val value: V)

fun <V> list(option: ListOption<V>, value: V) =
	List(option, value)

infix fun <V> List<V>.plus(value: V): List<V> =
	list(option(this), value)

fun <V> list(value: V, vararg values: V): List<V> =
	list(listOption(), value).fold(values) { plus(it) }

fun <V, R> R.fold(list: List<V>, fn: R.(V) -> R): R =
	fn(list.value).fold(list.option, fn)

val <V> List<V>.reverse get() =
	list(value).fold(option) { plus(it) }
