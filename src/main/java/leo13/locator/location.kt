package leo13.locator

import leo13.script.lineTo
import leo13.script.script

data class Location(val line: Line, val column: Column) {
	override fun toString() = scriptLine.toString()
}

fun location(line: Line = line(1), column: Column = column(1)) = Location(line, column)

val Location.scriptLine
	get() =
		"location" lineTo script(line.scriptLine, column.scriptLine)

val Location.newline
	get() =
		location(line.next, column(1))

val Location.nextChar
	get() =
		location(line, column.next)

fun Location.plus(char: Char) =
	if (char == '\n') newline
	else nextChar