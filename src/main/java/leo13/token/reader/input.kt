package leo13.token.reader

import leo13.LeoObject
import leo13.script.lineTo
import leo13.script.script

data class Input(val line: Line, val name: String) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "input"
	override val scriptableBody get() = script(line.scriptableLine, name lineTo script())
}

fun input(line: Line, name: String) = Input(line, name)
