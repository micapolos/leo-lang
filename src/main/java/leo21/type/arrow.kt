package leo21.type

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script

data class Arrow(val lhs: Type, val rhs: Type) : Scriptable() {
	override fun toString() = reflectScriptLine.toString()
	override val reflectScriptLine: ScriptLine
		get() = "arrow" lineTo script(
			"lhs" lineTo script(lhs.reflectScriptLine),
			"rhs" lineTo script(rhs.reflectScriptLine))
}

infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
