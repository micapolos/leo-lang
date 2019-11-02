package leo13.lambda.js

import leo.base.failIfOr
import leo13.js2.code
import leo13.lambda.Gen
import leo13.lambda.gen

fun Value.code(gen: Gen) = expr(gen).code
val Value.code get() = code(gen)

val Int.varCode
	get() =
		failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

