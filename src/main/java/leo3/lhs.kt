package leo3

object Lhs

val lhs = Lhs
fun Lhs.apply(parameter: Parameter): Result? =
	parameter.termOrNull?.run { result(lhs) }
