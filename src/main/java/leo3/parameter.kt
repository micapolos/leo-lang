package leo3

data class Parameter(val nodeOrNull: Node?)

fun parameter(nodeOrNull: Node?) = Parameter(nodeOrNull)