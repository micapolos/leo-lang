package leo32.base

data class Leaf<T>(val value: T)

val <T> T.toLeaf get() = Leaf(this)
operator fun <V> Leaf<V>.invoke() = value
fun <V> leaf(value: V) = Leaf(value)

