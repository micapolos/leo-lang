package leo19.term.eval

import leo13.get
import leo19.expr.ArrayExpr
import leo19.expr.ArrayGetExpr
import leo19.expr.Expr
import leo19.expr.FunctionExpr
import leo19.expr.IntExpr
import leo19.expr.InvokeExpr
import leo19.expr.NullExpr
import leo19.expr.VariableExpr
import leo19.term.Term
import leo19.term.expr.expr
import leo19.term.nullTerm
import leo19.term.term
import leo19.value.ArrayValue
import leo19.value.FunctionValue
import leo19.value.IntValue
import leo19.value.NullValue
import leo19.value.Value
import leo19.value.function
import leo19.value.value

val Term.eval get() = expr.eval
val Expr.eval get() = emptyScope.eval(this)

fun Scope.eval(term: Term) = eval(term.expr)
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

val Value.term: Term
	get() =
		when (this) {
			NullValue -> nullTerm
			is IntValue -> term(int)
			is ArrayValue -> term(*list.map { it.term }.toTypedArray())
			is FunctionValue -> null
		}!!