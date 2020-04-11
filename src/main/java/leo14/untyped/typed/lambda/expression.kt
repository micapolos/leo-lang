package leo14.untyped.typed.lambda

import leo14.lambda2.*

sealed class Expression

data class IntExpression(val int: Int) : Expression()
object IntOrIntExpression : Expression()
object IntAndIntExpression : Expression()
object IntShlIntExpression : Expression()
object IntUshrIntExpression : Expression()

val Term.unsafeExpression: Expression get() = value as Expression
val Int.expression: Expression get() = IntExpression(this)
val intOrIntExpression: Expression = IntOrIntExpression
val intAndIntExpression: Expression = IntAndIntExpression
val intShlIntExpression: Expression = IntShlIntExpression
val intUshrIntExpression: Expression = IntUshrIntExpression
val Expression.unsafeInt: Int get() = (this as IntExpression).int

fun Thunk.expressionApply(thunk: Thunk): Thunk? =
	(term as? ApplicationTerm)?.let { app ->
		(app.lhs as? ValueTerm)?.value?.let { lhsValue ->
			(lhsValue as? IntOrIntExpression)?.let {
				(app.rhs.value as IntExpression).int
					.or((thunk.term.value as IntExpression).int)
					.expression.valueTerm.thunk
			}
		}
	}
