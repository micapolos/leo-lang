package leo13.value

import leo13.LeoObject
import leo13.script.script

data class Set(val line: ExprLine) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "set"
	override val scriptableBody get() = script(line.scriptableLine)
}

fun set(line: ExprLine) = Set(line)