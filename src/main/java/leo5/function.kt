package leo5

data class Function(val pattern: Pattern, val body: Body)

fun function(pattern: Pattern, body: Body) = Function(pattern, body)