package leo13

import leo.base.fold
import leo.base.updateIfNotNull

data class ListOption<out V>(val listOrNull: List<V>?)

fun <V> option(list: List<V>) = ListOption(list)

fun <V> listOption(vararg values: V): ListOption<V> =
	ListOption<V>(null).fold(values) { plus(it) }

infix fun <V> ListOption<V>.plus(value: V): ListOption<V> =
	ListOption(list(this, value))

fun <V, R> R.fold(listOption: ListOption<V>, fn: R.(V) -> R): R =
	updateIfNotNull(listOption.listOrNull) { fold(it, fn) }

val <V> ListOption<V>.reverse get() =
	listOption<V>().fold(this) { plus(it) }
