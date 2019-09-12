package leo13.untyped.expression

import leo13.script.lineTo
import leo13.untyped.bindName

data class Bind(val expression: Expression)

fun bind(expression: Expression) = Bind(expression)

val Bind.scriptLine
	get() =
		bindName lineTo expression.bodyScript