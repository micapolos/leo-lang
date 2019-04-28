package leo32.treo

data class Capture(val variable: Var)

fun capture(variable: Var) = Capture(variable)