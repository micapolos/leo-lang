package leo13

import leo13.script.script

object Colon : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "colon"
	override val scriptableBody get() = script()
}

val colon = Colon
