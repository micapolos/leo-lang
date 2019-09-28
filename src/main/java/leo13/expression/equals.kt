package leo13.expression

import leo13.ObjectScripting
import leo13.equalsName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Equals(val expression: Expression) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = equalsName lineTo script(expression.scriptLine)
}

fun equals(expression: Expression) = Equals(expression)