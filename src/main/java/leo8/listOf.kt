package leo8

import leo.base.Empty
import leo.base.empty
import leo.base.foldRight

sealed class ListOf<out V>

data class EmptyListOf<V>(val empty: Empty) : ListOf<V>()
data class LinkListOf<V>(val link: ListLinkOf<V>) : ListOf<V>()

fun <V> listOf(empty: Empty): ListOf<V> = EmptyListOf(empty)
fun <V> list(link: ListLinkOf<V>): ListOf<V> = LinkListOf(link)

fun <V> V.plus(listOf: ListOf<V>) = list(link(this, listOf))

fun <V> listOf(vararg values: V): ListOf<V> = listOf<V>(empty).foldRight(values) { plus(it) }
