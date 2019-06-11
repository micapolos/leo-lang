package leo5

data class Function(val type: Type, val body: Body)

fun function(type: Type, body: Body) = Function(type, body)