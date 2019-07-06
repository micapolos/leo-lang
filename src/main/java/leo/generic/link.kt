package leo.generic

data class Link<out V>(val head: Head<V>, val tail: Tail<List<V>>)

infix fun <V> Head<V>.linkTo(tail: Tail<List<V>>) = Link(this, tail)