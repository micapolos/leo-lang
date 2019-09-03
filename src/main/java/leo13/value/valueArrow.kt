package leo13.value

data class ValueArrow(val lhs: Value, val rhs: Value)

fun arrow(lhs: Value, rhs: Value) = ValueArrow(lhs, rhs)
