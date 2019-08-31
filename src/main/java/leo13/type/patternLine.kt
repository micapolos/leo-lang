package leo13.type

import leo13.script.ScriptLine
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class PatternLine(val name: String, val rhs: PatternRhs) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithMeta(meta: Boolean): ScriptLine =
		if (meta) "meta" lineTo script(scriptableLine)
		else scriptableLine
}

infix fun String.lineTo(rhs: PatternRhs) = PatternLine(this, rhs)
infix fun String.lineTo(rhs: Pattern) = lineTo(thunk(rhs))

fun PatternLine.contains(line: PatternLine): Boolean =
	name == line.name && rhs.contains(line.rhs)

val ScriptLine.unsafePatternLine: PatternLine
	get() =
		name lineTo rhs.unsafePattern

