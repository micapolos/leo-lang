package leo13.locator

import leo13.ScriptingObject
import leo13.script.lineTo
import leo13.script.script
import leo13.scriptLine

data class Column(val int: Int) : ScriptingObject() {
	override fun toString() = super.toString()
	override val scriptingLine get() = "column" lineTo script(int.scriptLine)

}

fun column(int: Int) = Column(int)
val Column.next get() = column(int + 1)
