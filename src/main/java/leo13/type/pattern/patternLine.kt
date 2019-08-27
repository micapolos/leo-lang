package leo13.type.pattern

import leo13.script.ScriptLine
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class PatternLine(val name: String, val rhs: Pattern) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	val firstScriptableLine: ScriptLine
		get() =
			if (name == "choice" || name == "arrow") "meta" lineTo script(scriptableLine)
			else scriptableLine
}

infix fun String.lineTo(rhs: Pattern) = PatternLine(this, rhs)

fun PatternLine.contains(line: PatternLine): Boolean =
	name == line.name && rhs.contains(line.rhs)
