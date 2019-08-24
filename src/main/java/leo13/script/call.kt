package leo13.script

import leo13.Script
import leo13.script.evaluator.Bindings
import leo13.script.evaluator.push

data class Call(val expr: Expr)

fun call(expr: Expr) = Call(expr)

fun Call.eval(bindings: Bindings, lhs: Script): Script =
	expr.eval(bindings.push(lhs))