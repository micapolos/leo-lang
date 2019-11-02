package leo13.js2.lambda

import leo.base.failIfOr
import leo13.js2.code

data class JsGen(val depth: Int)

fun jsGen(depth: Int) = JsGen(depth)
val jsGen = jsGen(0)
val JsGen.inc get() = JsGen(depth.inc())
fun <T> JsGen.inc(fn: (JsGen) -> T) = fn(inc)

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsArrow = Arrow<JsExpr>
typealias JsLhs = Lhs<JsExpr>
typealias JsRhs = Rhs<JsExpr>
typealias JsFn = Fn<JsExpr>
typealias JsAp = Ap<JsExpr>
typealias JsArg = Arg<Js>

val jsArg = arg<Js>()

val JsExpr.jsCode get() = code(jsGen)

fun JsExpr.code(gen: JsGen): String =
	when (this) {
		is ValueExpr -> value.code
		is ArrowExpr -> arrow.exprCode(gen)
		is LhsExpr -> lhs.exprCode(gen)
		is RhsExpr -> rhs.exprCode(gen)
		is FnExpr -> fn.exprCode(gen)
		is ApExpr -> ap.exprCode(gen)
		is ArgExpr -> arg.exprCode(gen)
	}

fun JsArrow.exprCode(gen: JsGen) =
	"[${lhs.code(gen)}, ${rhs.code(gen)}]"

fun JsLhs.exprCode(gen: JsGen) =
	"(${value.code(gen)})[0]"

fun JsRhs.exprCode(gen: JsGen) =
	"(${value.code(gen)})[1]"

fun JsFn.exprCode(gen: JsGen) =
	"${paramCode(gen)} => ${gen.inc { body.code(it) }}"

fun JsAp.exprCode(gen: JsGen) =
	"(${lhs.code(gen)})(${rhs.code(gen)})"

fun JsArg.exprCode(gen: JsGen) =
	index(gen).jsVarCode

fun JsArg.index(gen: JsGen) =
	gen.depth - index - 1

fun paramCode(gen: JsGen) =
	gen.depth.jsVarCode

val Int.jsVarCode
	get() =
		failIfOr(this < 0) { "v$this" }
