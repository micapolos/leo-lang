package leo19.value

import leo13.get
import leo19.expr.ArrayExpr
import leo19.expr.ArrayGetExpr
import leo19.expr.Expr
import leo19.expr.FunctionExpr
import leo19.expr.IntExpr
import leo19.expr.InvokeExpr
import leo19.expr.NullExpr
import leo19.expr.VariableExpr

val Expr.eval get() = emptyScope.eval(this)

fun Scope.eval(expr: Expr): Value =
	when (expr) {
		NullExpr -> NullValue
		is IntExpr -> value(expr.int)
		is ArrayExpr -> ArrayValue(expr.list.map { eval(it) })
		is ArrayGetExpr -> (eval(expr.array) as ArrayValue).list[(eval(expr.index) as IntValue).int]
		is FunctionExpr -> value(function(this, expr.body))
		is InvokeExpr -> (eval(expr.function) as FunctionValue).function.let { function ->
			function.scope.push(eval(expr.param)).eval(function.body)
		}
		is VariableExpr -> stack.get(expr.index)!!
	}
