package leo13.pattern

import leo13.ObjectScripting
import leo13.functionName
import leo13.script.lineTo
import leo13.script.script

data class PatternFunction(val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = functionName lineTo script(pattern.scriptingLine)
}

fun function(pattern: Pattern) = PatternFunction(pattern)
