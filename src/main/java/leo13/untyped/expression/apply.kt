package leo13.untyped.expression

data class Apply(val expression: Expression)

fun apply(expression: Expression) = Apply(expression)
