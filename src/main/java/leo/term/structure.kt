package leo.term

import leo.base.Stack
import leo.base.map

data class Structure<out V>(
	val fieldStack: Stack<Field<V>>)

val <V> Stack<Field<V>>.structure: Structure<V>
	get() =
		Structure(this)

fun <V, R> Structure<V>.map(fn: V.() -> R): Structure<R> =
	fieldStack.map { it.map(fn) }.structure