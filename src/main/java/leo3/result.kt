package leo3

data class Result(val termOrNull: Term?)

fun result(termOrNull: Term?) = Result(termOrNull)