package leo13.interpreter

import leo13.ObjectScripting
import leo13.interpretedName
import leo13.pattern.Pattern
import leo13.pattern.pattern
import leo13.script.lineTo
import leo13.script.script
import leo13.value.Value
import leo13.value.scriptLine
import leo13.value.value

data class Interpreted(val value: Value, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() = interpretedName lineTo script(
			value.scriptLine, pattern.scriptingLine)
}

fun interpreted(value: Value, pattern: Pattern) = Interpreted(value, pattern)
fun interpreted() = interpreted(value(), pattern())