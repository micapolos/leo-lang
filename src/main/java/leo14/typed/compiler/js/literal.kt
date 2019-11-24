package leo14.typed.compiler.js

import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.typed.TypedLine
import leo14.typed.lineTo
import leo14.typed.nativeTyped
import leo14.typed.textName

val Literal.typedLine: TypedLine<Expr>
	get() =
		when (this) {
			is StringLiteral -> string.typedLine
			is NumberLiteral -> number.typedLine
		}

val String.typedLine: TypedLine<Expr>
	get() =
		textName lineTo nativeTyped(expr(this))

val Number.typedLine: TypedLine<Expr>
	get() =
		"number" lineTo nativeTyped(expr(double))

// No need for decompiling, since we don't evaluate.
val TypedLine<Expr>.decompileLiteral: Literal?
	get() =
		null