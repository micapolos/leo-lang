package leo13

import leo13.type.TypeLine
import leo13.script.lineTo
import leo13.script.script

object Empty : ObjectScripting() {
	override fun toString() = scriptingLine.toString()
	override val scriptingLine get() = "empty" lineTo script()
	fun contains(line: TypeLine) = false
}

val empty = Empty
