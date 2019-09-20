package leo13.expression

import leo13.bindName
import leo13.script.lineTo

data class Give(val expression: Expression)

fun bind(expression: Expression) = Give(expression)

val Give.scriptLine
	get() =
		bindName lineTo expression.bodyScript