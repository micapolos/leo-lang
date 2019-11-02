package leo13.js.compiler

data class Bound(val index: Int)

fun bound(index: Int) = Bound(index)

val Bound.code get() = "__bound($index)"