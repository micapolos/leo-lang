package leo13.type

import leo13.script.Scriptable

data class Recursion(val lhsOrNull: Recursion?) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "recursion"
	override val scriptableBody get() = TODO()
}

val recursion = Recursion(null)
val Recursion?.recursion get() = Recursion(this)
