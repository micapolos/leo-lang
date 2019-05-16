package leo3

object Rhs

val rhs = Rhs
fun Rhs.apply(parameter: Parameter): Result? =
	parameter.termOrNull?.run { result(rhs) }
