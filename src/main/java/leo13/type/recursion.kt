package leo13.type

import leo13.LeoObject
import leo13.script.script

data class Recursion(val lhsOrNull: Recursion?) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "recursion"
	override val scriptableBody get() = lhsOrNull?.run { script(scriptableLine) } ?: script()
}

val recursion = Recursion(null)
val Recursion?.recursion get() = Recursion(this)
