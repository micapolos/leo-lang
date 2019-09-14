package leo13.locator

import leo13.ScriptingObject
import leo13.script.lineTo
import leo13.script.script

data class Location(val line: Line, val column: Column) : ScriptingObject() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "location" lineTo script(line.scriptingLine, column.scriptingLine)
}

fun location(line: Line = line(1), column: Column = column(1)) = Location(line, column)

val Location.newline
	get() =
		location(line.next, column(1))

val Location.nextChar
	get() =
		location(line, column.next)

fun Location.plus(char: Char) =
	if (char == '\n') newline
	else nextChar