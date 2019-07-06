package leo.generic

import leo.base.Empty

sealed class List<out V>

data class EmptyList<V>(val empty: Empty) : List<V>()
data class LinkList<V>(val link: Link<V>) : List<V>()

fun <V> list(empty: Empty): List<V> = EmptyList(empty)
fun <V> list(link: Link<V>): List<V> = LinkList(link)
