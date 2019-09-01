package leo13.token.reader

import leo13.LeoObject
import leo13.Tab
import leo13.script.script

object New : LeoObject() {
	override fun toString() = Tab.scriptableLine.toString()
	override val scriptableName get() = "new"
	override val scriptableBody get() = script()
}

val new = New
