package leo13

data class AtomFunction(val expression: Expression)

fun function(expression: Expression) = AtomFunction(expression)

