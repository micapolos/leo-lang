package leo13.locator

import leo13.ScriptingObject
import leo13.script.lineTo
import leo13.script.script
import leo13.scriptLine

data class Line(val int: Int) : ScriptingObject() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "line" lineTo script(int.scriptLine)
}

fun line(int: Int) = Line(int)
val Line.next get() = line(int + 1)
