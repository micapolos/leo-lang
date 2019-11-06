package leo14.lambda.js

import leo.base.failIfOr
import leo13.int
import leo14.js.ast.Expr
import leo14.js.ast.code
import leo14.lambda.Variable
import leo14.lambda.code.Gen
import leo14.lambda.code.gen

fun Term.code(gen: Gen) = expr(gen).code
val Term.code get() = code(gen)

val Int.varCode
	get() =
		failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

fun Variable<Expr>.index(gen: Gen) =
	gen.depth - index.int - 1
