package leo13.lambda.js

import leo13.js2.Expr
import leo13.lambda.Gen
import leo13.lambda.Variable

typealias Value = leo13.lambda.Value<Expr>

fun Variable<Expr>.index(gen: Gen) =
	gen.depth - index - 1
