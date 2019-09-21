package leo13.pattern

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class PatternGiven(val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			"given" lineTo script(pattern.scriptingLine)

	fun give(pattern: Pattern) =
		PatternGiven(pattern.plus("given" lineTo pattern))
}

fun patternGiven() = PatternGiven(pattern())