package leo13.lambda.js

import leo.base.failIfOr
import leo13.js2.code

fun JsExpr.code(gen: Gen) = js(gen).code
val JsExpr.jsCode get() = code(gen)

val Int.jsVarCode
	get() =
		failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.jsVarCode

