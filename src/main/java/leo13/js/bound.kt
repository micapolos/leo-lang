package leo13.js

data class Bound(val index: Int)

fun bound(index: Int) = Bound(index)

val Bound.code get() = "__bound($index)"