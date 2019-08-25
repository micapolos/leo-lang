package leo13.script

import leo13.AsScriptLine
import leo13.ScriptLine
import leo13.lineTo
import leo13.script

data class Error(val scriptLine: ScriptLine) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "error" lineTo script(scriptLine)
}

fun error(scriptLine: ScriptLine) = Error(scriptLine)
