package leo13.pattern

import leo13.ObjectScripting
import leo13.contextName
import leo13.script.lineTo
import leo13.script.script

data class PatternContext(val given: PatternGiven) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine
		get() =
			contextName lineTo script(given.scriptingLine)

	fun give(pattern: Pattern) =
		PatternContext(given.give(pattern))
}

fun patternContext() = PatternContext(patternGiven())

