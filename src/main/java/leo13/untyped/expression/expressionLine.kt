package leo13.untyped.expression

import leo13.script.lineTo

data class ExpressionLine(val name: String, val rhs: Expression)

infix fun String.lineTo(rhs: Expression) = ExpressionLine(this, rhs)

val ExpressionLine.bodyScriptLine
	get() =
		name lineTo rhs.bodyScript