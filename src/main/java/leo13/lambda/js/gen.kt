package leo13.lambda.js

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

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1
