package leo13

import leo13.script.script

object Space : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "space"
	override val scriptableBody get() = script()
}

val space = Space
