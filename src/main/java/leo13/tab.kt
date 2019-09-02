package leo13

import leo13.script.script

object Tab : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "tab"
	override val scriptableBody get() = script()
}

val tab = Tab
