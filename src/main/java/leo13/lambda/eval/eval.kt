package leo13.lambda.eval

import leo13.get
import leo13.lambda.*
import leo13.stack

val Expr.eval get() = eval(stack())

fun Expr.eval(evalStack: Stack): Any =
	when (this) {
		is ValueExpr -> value
		is AbstractionExpr -> abstraction.eval(evalStack)
		is ApplicationExpr -> application.eval(evalStack)
		is VariableExpr -> variable.eval(evalStack)
	}

fun Abstraction<Expr>.eval(stack: Stack) = function(stack, body)
fun Application<Expr>.eval(stack: Stack) = (lhs.eval(stack) as Function)(rhs.eval(stack))
fun Variable<Any>.eval(stack: Stack) = stack.get(index)!!
