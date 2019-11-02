package leo13.lambda.js

import leo13.lambda.*

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsArrow = Arrow<JsExpr>
typealias JsLhs = Lhs<JsExpr>
typealias JsRhs = Rhs<JsExpr>
typealias JsFn = Fn<JsExpr>
typealias JsAp = Ap<JsExpr>
typealias JsArg = Arg<Js>

val jsArg = arg<Js>()

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1

fun jsExpr(int: Int) = expr(leo13.js2.expr(int))
fun jsExpr(double: Double) = expr(leo13.js2.expr(double))
fun jsExpr(string: String) = expr(leo13.js2.expr(string))

