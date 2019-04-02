package leo32.base

data class Ap(
	val lhs: Term,
	val rhsOrNull: Term?)

fun Term.ap(rhsOrNull: Term? = null) =
	Ap(this, rhsOrNull)

