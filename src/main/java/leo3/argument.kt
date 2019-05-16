package leo3

object Argument

val argument = Argument
fun Argument.apply(parameter: Parameter) = result(parameter.termOrNull)