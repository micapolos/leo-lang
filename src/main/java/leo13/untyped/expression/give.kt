package leo13.untyped.expression

import leo13.script.lineTo
import leo13.untyped.bindName

data class Give(val expression: Expression)

fun bind(expression: Expression) = Give(expression)

val Give.scriptLine
	get() =
		bindName lineTo expression.bodyScript