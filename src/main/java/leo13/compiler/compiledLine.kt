package leo13.compiler

import leo13.Scriptable
import leo13.lineTo
import leo13.script

data class CompiledLine(
	val name: String,
	val rhs: Metable) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo script("to" lineTo script(rhs.scriptableLine)))
}

infix fun String.lineTo(rhs: Metable) = CompiledLine(this, rhs)
