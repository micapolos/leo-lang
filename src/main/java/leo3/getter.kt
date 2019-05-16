package leo3

sealed class Getter
data class LhsGetter(val lhs: Lhs) : Getter()
data class RhsGetter(val rhs: Rhs) : Getter()

fun getter(lhs: Lhs): Getter = LhsGetter(lhs)
fun getter(rhs: Rhs): Getter = RhsGetter(rhs)

fun Getter.apply(script: Script): Script =
	when (this) {
		is LhsGetter -> lhs.apply(script)
		is RhsGetter -> rhs.apply(script)
	}
