package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class Fix(val expression: Expression) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "fix" lineTo script(expression.scriptLine)
}

fun fix(expression: Expression) = Fix(expression)