package leo13.lambda.js

import leo.base.failIfOr
import leo13.js2.code
import leo13.lambda.*

data class Gen(val depth: Int)

fun gen(depth: Int) = Gen(depth)
val gen = gen(0)
val Gen.inc get() = Gen(depth.inc())
fun <T> Gen.inc(fn: (Gen) -> T) = fn(inc)

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsArrow = Arrow<JsExpr>
typealias JsLhs = Lhs<JsExpr>
typealias JsRhs = Rhs<JsExpr>
typealias JsFn = Fn<JsExpr>
typealias JsAp = Ap<JsExpr>
typealias JsArg = Arg<Js>

val jsArg = arg<Js>()

val JsExpr.jsCode get() = code(gen)

fun JsExpr.code(gen: Gen): String =
	when (this) {
		is ValueExpr -> value.code
		is ArrowExpr -> arrow.exprCode(gen)
		is LhsExpr -> lhs.exprCode(gen)
		is RhsExpr -> rhs.exprCode(gen)
		is FnExpr -> fn.exprCode(gen)
		is ApExpr -> ap.exprCode(gen)
		is ArgExpr -> arg.exprCode(gen)
	}

fun JsArrow.exprCode(gen: Gen) =
	"[${lhs.code(gen)}, ${rhs.code(gen)}]"

fun JsLhs.exprCode(gen: Gen) =
	"(${value.code(gen)})[0]"

fun JsRhs.exprCode(gen: Gen) =
	"(${value.code(gen)})[1]"

fun JsFn.exprCode(gen: Gen) =
	"${paramCode(gen)} => ${gen.inc { body.code(it) }}"

fun JsAp.exprCode(gen: Gen) =
	"(${lhs.code(gen)})(${rhs.code(gen)})"

fun JsArg.exprCode(gen: Gen) =
	index(gen).jsVarCode

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1

fun paramCode(gen: Gen) =
	gen.depth.jsVarCode

val Int.jsVarCode
	get() =
		failIfOr(this < 0) { "v$this" }
