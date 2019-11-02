package leo13.lambda.js

import leo13.js2.*
import leo13.lambda.*
import leo13.lambda.ApExpr
import leo13.lambda.FnExpr

val JsExpr.js get() = js(gen)

fun JsExpr.js(gen: Gen): leo13.js2.Expr =
	when (this) {
		is ValueExpr -> value
		is ArrowExpr -> arrow.js(gen)
		is LhsExpr -> lhs.js(gen)
		is RhsExpr -> rhs.js(gen)
		is FnExpr -> fn.js(gen)
		is ApExpr -> ap.js(gen)
		is ArgExpr -> arg.js(gen)
	}

fun JsArrow.js(gen: Gen) = expr(arr(lhs.js(gen), rhs.js(gen)))
fun JsLhs.js(gen: Gen) = value.js(gen)[expr(0)]
fun JsRhs.js(gen: Gen) = value.js(gen)[expr(1)]
fun JsFn.js(gen: Gen) = paramCode(gen) ret gen.inc { body.js(it) }
fun JsAp.js(gen: Gen) = lhs.js(gen).invoke(rhs.js(gen))
fun JsArg.js(gen: Gen) = expr(id(index(gen).jsVarCode))