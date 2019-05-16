package leo3

data class Result(val nodeOrNull: Node?)

fun result(nodeOrNull: Node?) = Result(nodeOrNull)