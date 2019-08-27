package leo13.type.pattern

import leo13.script.Scriptable
import leo13.script.plus
import leo13.script.script

data class PatternLink(val lhs: Pattern, val line: PatternLine) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody
		get() =
			if (lhs is EmptyPattern) script(line.firstScriptableLine)
			else lhs.scriptableBody.plus(line.scriptableLine)
}

fun link(lhs: Pattern, line: PatternLine) = PatternLink(lhs, line)

fun PatternLink.contains(link: PatternLink): Boolean =
	lhs.contains(link.lhs) && line.contains(link.line)
