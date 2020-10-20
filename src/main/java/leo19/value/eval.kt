package leo19.value

import leo.base.runIf
import leo13.get
import leo19.expr.ArrayExpr
import leo19.expr.ArrayGetExpr
import leo19.expr.EqualsExpr
import leo19.expr.Expr
import leo19.expr.FunctionExpr
import leo19.expr.IntExpr
import leo19.expr.InvokeExpr
import leo19.expr.LhsExpr
import leo19.expr.NullExpr
import leo19.expr.PairExpr
import leo19.expr.RhsExpr
import leo19.expr.VariableExpr

val Expr.eval get() = emptyScope.eval(this)

fun Scope.eval(expr: Expr): Value =
	when (expr) {
		NullExpr -> NullValue
		is IntExpr -> value(expr.int)
		is PairExpr -> eval(expr.lhs).to(eval(expr.rhs))
		is LhsExpr -> eval(expr.pair).resolveLhs
		is RhsExpr -> eval(expr.pair).resolveRhs
		is ArrayExpr -> ArrayValue(expr.list.map { eval(it) })
		is ArrayGetExpr -> eval(expr.array).resolveGet(eval(expr.index))
		is FunctionExpr -> value(function(this, expr.body))
		is InvokeExpr -> eval(expr.function).function.invoke(eval(expr.param))
		is VariableExpr -> stack.get(expr.index)!!
		is EqualsExpr -> value(if (value(eval(expr.lhs)) == value(eval(expr.rhs))) 0 else 1)
	}

val Value.resolveLhs get() = (this as PairValue).lhs
val Value.resolveRhs get() = (this as PairValue).rhs
fun Value.resolveGet(index: Value) = (this as ArrayValue).list[(index as IntValue).int]
val Value.function get() = (this as FunctionValue).function

fun Function.invoke(value: Value): Value =
	scope
		.runIf(isRecursive) { push(value(this@invoke)) }
		.push(value)
		.eval(body)
