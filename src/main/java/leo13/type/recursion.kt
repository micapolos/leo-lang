package leo13.type

import leo13.Scriptable
import leo13.script.script

data class Recursion(val lhsOrNull: Recursion?) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "recursion"
	override val scriptableBody get() = script()
}

val recursion = Recursion(null)
val Recursion?.recursion get() = Recursion(this)
