package leo13.token.reader

import leo13.LeoObject
import leo13.Tab
import leo13.script.script

object Same : LeoObject() {
	override fun toString() = Tab.scriptableLine.toString()
	override val scriptableName get() = "same"
	override val scriptableBody get() = script()
}

val same = Same
