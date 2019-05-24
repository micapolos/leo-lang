package leo3

sealed class Getter
data class LhsGetter(val lhs: Lhs) : Getter()
data class RhsGetter(val rhs: Rhs) : Getter()

fun getter(lhs: Lhs): Getter = LhsGetter(lhs)
fun getter(rhs: Rhs): Getter = RhsGetter(rhs)

fun Getter.apply(term: Term): Value =
	when (this) {
		is LhsGetter -> lhs.apply(term)
		is RhsGetter -> rhs.apply(term)
	}
