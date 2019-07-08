package leo8

data class ListLinkOf<out V>(val head: V, val tail: ListOf<V>)

fun <V> link(head: V, tail: ListOf<V>) = ListLinkOf(head, tail)
