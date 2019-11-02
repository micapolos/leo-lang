package leo13.js2.lambda

import leo.base.failIfOr
import leo13.js2.code

data class JsGen(val depth: Int)

val jsGen = JsGen(0)
fun <T> JsGen.bind(fn: (JsGen) -> T) = fn(JsGen(depth.inc()))
val JsGen.paramCode get() = depth.jsVarCode

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsArrow = Arrow<JsExpr>
typealias JsLhs = Lhs<JsExpr>
typealias JsRhs = Rhs<JsExpr>
typealias JsFn = Fn<JsExpr>
typealias JsAp = Ap<JsExpr>

fun jsArg(index: Int) = expr<Js>(arg(index))

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
	"${value.code(gen)}[0]"

fun JsRhs.exprCode(gen: JsGen) =
	"${value.code(gen)}[1]"

fun JsFn.exprCode(gen: JsGen) =
	"function(${gen.paramCode}) { return ${gen.bind { body.code(it) }}; }"

fun JsAp.exprCode(gen: JsGen) =
	"${lhs.code(gen)}(${rhs.code(gen)})"

fun Arg.exprCode(gen: JsGen) =
	"${(gen.depth - index - 1).jsVarCode}"

val Int.jsVarCode get() = failIfOr(this < 0) { "v$this" }
