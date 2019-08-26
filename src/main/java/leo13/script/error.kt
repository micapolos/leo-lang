package leo13.script

import leo13.ScriptLine
import leo13.Scriptable
import leo13.script

data class Error(val scriptLine: ScriptLine) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "error"
	override val scriptableBody get() = script(scriptLine)
}

fun error(scriptLine: ScriptLine) = Error(scriptLine)
