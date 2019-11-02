package leo13.lambda.js

import leo13.lambda.*

typealias Js = leo13.js2.Expr
typealias JsExpr = Expr<Js>
typealias JsFn = Abstraction<JsExpr>
typealias JsAp = Application<JsExpr>
typealias JsArg = Variable<Js>

val jsArg = variable<Js>()

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1
