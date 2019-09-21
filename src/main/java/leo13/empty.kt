package leo13

import leo13.pattern.PatternLine
import leo13.script.lineTo
import leo13.script.script

object Empty : ObjectScripting() {
	override fun toString() = scriptingLine.toString()
	override val scriptingLine get() = "empty" lineTo script()
	fun contains(line: PatternLine) = false
}

val empty = Empty
