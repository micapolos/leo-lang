package leo13.expression

import leo13.doName
import leo13.script.lineTo

data class Apply(val expression: Expression)

fun apply(expression: Expression) = Apply(expression)

val Apply.scriptLine
	get() =
		doName lineTo expression.bodyScript