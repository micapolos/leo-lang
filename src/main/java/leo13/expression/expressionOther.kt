package leo13.expression

import leo13.ObjectScripting
import leo13.otherName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class ExpressionOther(val expression: Expression) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = otherName lineTo script(expression.scriptLine)
}

fun other(expression: Expression) = ExpressionOther(expression)