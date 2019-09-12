package leo13.untyped.expression

data class Bind(val expression: Expression)

fun bind(expression: Expression) = Bind(expression)
