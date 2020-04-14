package leo15.core

sealed class Expression
data class IntExpression(val int: Int) : Expression()
data class ShlIntExpression(val rhs: Expression) : Expression()
data class UShrIntExpression(val rhs: Expression) : Expression()
data class OrIntExpression(val rhs: Expression) : Expression()
data class AndIntExpression(val rhs: Expression) : Expression()

val Int.expression: Expression get() = IntExpression(this)

fun Expression.apply(expression: Expression): Expression =
	when (this) {
		is IntExpression ->
			when (expression) {
				is IntExpression -> null!!
				is ShlIntExpression -> int.shl(expression.rhs.int).expression
				is UShrIntExpression -> int.ushr(expression.rhs.int).expression
				is OrIntExpression -> int.or(expression.rhs.int).expression
				is AndIntExpression -> int.and(expression.rhs.int).expression
			}
		is ShlIntExpression -> null!!
		is UShrIntExpression -> TODO()
		is OrIntExpression -> null!!
		is AndIntExpression -> TODO()
	}

val Expression.int: Int get() = (this as IntExpression).int