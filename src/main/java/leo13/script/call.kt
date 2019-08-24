package leo13.script

import leo13.Script

data class Call(val expr: Expr)

fun call(expr: Expr) = Call(expr)

fun Call.eval(bindings: Bindings, lhs: Script): Script =
	expr.eval(bindings.push(lhs))