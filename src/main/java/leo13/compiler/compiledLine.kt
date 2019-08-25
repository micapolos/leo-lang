package leo13.compiler

import leo13.AsScriptLine
import leo13.lineTo
import leo13.script

data class CompiledLine(
	val name: String,
	val rhs: Metable) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "line" lineTo script(name lineTo script("to" lineTo script(rhs.asScriptLine)))
}

infix fun String.lineTo(rhs: Metable) = CompiledLine(this, rhs)
