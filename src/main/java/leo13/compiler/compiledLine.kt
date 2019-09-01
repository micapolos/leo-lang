package leo13.compiler

import leo13.Scriptable

data class CompiledLine(val name: String, val rhs: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
}

infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)
