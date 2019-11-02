package leo13.lambda.js

import leo13.js2.expr
import leo13.js2.id
import leo13.js2.invoke
import leo13.js2.ret
import leo13.lambda.*

val JsExpr.js get() = js(gen)

fun JsExpr.js(gen: Gen): leo13.js2.Expr =
	when (this) {
		is ValueExpr -> value
		is AbstractionExpr -> abstraction.js(gen)
		is ApplicationExpr -> application.js(gen)
		is VariableExpr -> variable.js(gen)
	}

fun JsFn.js(gen: Gen) = paramCode(gen) ret gen.inc { body.js(it) }
fun JsAp.js(gen: Gen) = lhs.js(gen).invoke(rhs.js(gen))
fun JsArg.js(gen: Gen) = expr(id(index(gen).jsVarCode))