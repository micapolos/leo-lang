package leo13.lambda.js

import leo13.lambda.*

typealias Js = leo13.js2.Expr
typealias JsValue = Value<Js>
typealias JsFn = Abstraction<JsValue>
typealias JsAp = Application<JsValue>
typealias JsArg = Variable<Js>

val jsArg = variable<Js>()

fun JsArg.index(gen: Gen) =
	gen.depth - index - 1
