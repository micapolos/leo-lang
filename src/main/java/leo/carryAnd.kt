package leo

data class CarryAnd<T>(
	val carry: Carry,
	val that: T)

fun <T> Carry.and(that: T) =
	CarryAnd(this, that)