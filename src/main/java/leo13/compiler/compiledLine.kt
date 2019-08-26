package leo13.compiler

import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class CompiledLine(
	val name: String,
	val rhs: Metable) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo script("to" lineTo script(rhs.scriptableLine)))
}

infix fun String.lineTo(rhs: Metable) = CompiledLine(this, rhs)
