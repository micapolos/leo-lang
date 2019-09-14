package leo13.locator

import leo13.script.lineTo
import leo13.script.script
import leo13.scriptLine

data class Line(val int: Int) {
	override fun toString() = scriptLine.toString()
}

fun line(int: Int) = Line(int)
val Line.next get() = line(int + 1)

val Line.scriptLine
	get() =
		"line" lineTo script(int.scriptLine)