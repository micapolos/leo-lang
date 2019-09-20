package leo13.untyped.expression

import leo13.applyName
import leo13.script.lineTo

data class Apply(val expression: Expression)

fun apply(expression: Expression) = Apply(expression)

val Apply.scriptLine
	get() =
		applyName lineTo expression.bodyScript