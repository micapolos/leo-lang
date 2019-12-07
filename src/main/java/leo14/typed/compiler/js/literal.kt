package leo14.typed.compiler.js

import leo14.Literal
import leo14.lambda.js.expr.Expr
import leo14.lambda.js.expr.expr
import leo14.lambda.js.expr.term
import leo14.typed.TypedLine
import leo14.typed.of
import leo14.typed.typeLine

val Literal.typedLine: TypedLine<Expr>
	get() =
		expr.term of typeLine
