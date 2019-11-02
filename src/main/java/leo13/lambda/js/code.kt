package leo13.lambda.js

import leo.base.failIfOr
import leo13.js2.Expr
import leo13.js2.code
import leo13.lambda.Variable
import leo13.lambda.code.Gen
import leo13.lambda.code.gen

fun Value.code(gen: Gen) = expr(gen).code
val Value.code get() = code(gen)

val Int.varCode
	get() =
		failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

fun Variable<Expr>.index(gen: Gen) =
	gen.depth - index - 1
