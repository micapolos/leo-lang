package leo14.typed.compiler.js

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.lambda.term
import leo14.typed.*

val Literal.typedLine: TypedLine<Expr>
	get() =
		term(expr) of expressionLine

val Literal.expr: Expr
	get() =
		when (this) {
			is StringLiteral -> string.expr
			is NumberLiteral -> number.expr
		}

val String.expr: Expr
	get() =
		expr(this)

val Number.expr: Expr
	get() =
		expr(bigDecimal.toDouble())

// No need for decompiling, since we don't evaluate.
val TypedLine<Expr>.decompileLiteral: Literal?
	get() =
		null