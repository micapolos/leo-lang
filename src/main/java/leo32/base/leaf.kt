package leo32.base

data class Leaf<T>(val value: T)

val <T> T.leaf get() = Leaf(this)