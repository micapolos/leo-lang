package leo3

data class Parameter(val termOrNull: Term?)

fun parameter(termOrNull: Term?) = Parameter(termOrNull)