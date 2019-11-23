package leo14.typed.compiler.js

import leo14.*
import leo14.Number
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.typed.TypedLine
import leo14.typed.lineTo
import leo14.typed.nativeTyped

val Literal.typedLine: TypedLine<Expr>
	get() =
		when (this) {
			is StringLiteral -> string.typedLine
			is NumberLiteral -> number.typedLine
		}

val String.typedLine: TypedLine<Expr>
	get() =
		"string" lineTo nativeTyped(expr(this))

val Number.typedLine: TypedLine<Expr>
	get() =
		when (this) {
			is IntNumber -> int.typedLine
			is DoubleNumber -> double.typedLine
		}

val Int.typedLine: TypedLine<Expr>
	get() =
		"int" lineTo nativeTyped(expr(this))

val Double.typedLine: TypedLine<Expr>
	get() =
		"double" lineTo nativeTyped(expr(this))

// No need for decompiling, since we don't evaluate.
val TypedLine<Expr>.decompileLiteral: Literal?
	get() =
		null