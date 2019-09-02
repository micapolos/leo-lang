package leo13

import leo13.script.script

object Newline : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "newline"
	override val scriptableBody get() = script()
}

val newline = Newline
