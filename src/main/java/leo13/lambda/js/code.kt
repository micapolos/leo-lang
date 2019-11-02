package leo13.lambda.js

import leo.base.failIfOr
import leo13.js2.code
import leo13.lambda.Gen
import leo13.lambda.gen

fun JsValue.code(gen: Gen) = js(gen).code
val JsValue.jsCode get() = code(gen)

val Int.jsVarCode
	get() =
		failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.jsVarCode

