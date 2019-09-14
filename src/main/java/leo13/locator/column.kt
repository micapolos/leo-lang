package leo13.locator

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.scriptLine

data class Column(val int: Int) {
	override fun toString() = scriptLine.toString()
}

fun column(int: Int) = Column(int)
val Column.next get() = column(int + 1)

val Column.scriptLine: ScriptLine
	get() =
		"column" lineTo script(int.scriptLine)