package leo13

data class ExpressionCompiled(
	val expression: Expression,
	val pattern: Pattern)

fun match(expression: Expression, pattern: Pattern) =
	ExpressionCompiled(expression, pattern)
