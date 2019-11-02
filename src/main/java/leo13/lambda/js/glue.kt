package leo13.lambda.js

import leo13.lambda.*

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsFn = Fn<JsExpr>
typealias JsAp = Ap<JsExpr>
typealias JsArg = Arg<Js>

val jsArg = arg<Js>()

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1
