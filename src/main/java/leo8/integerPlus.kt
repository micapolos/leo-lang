package leo8

data class IntegerPlus(val lhs: Integer, val rhs: Integer)

fun Integer.plus(integer: Integer) = IntegerPlus(this, integer)
