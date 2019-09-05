package leo13.untyped

import leo13.LeoStruct
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.script
import leo9.EmptyStack
import leo9.LinkStack

data class pattern(val script: Script = script()) : LeoStruct("pattern", script) {
	override fun toString() = super.toString()
}

fun Script.matches(pattern: pattern): Boolean =
	matches(pattern.script)

fun Script.matches(pattern: Script): Boolean =
	null
		?: matchesAnyOrNull(pattern)
		?: matchesPlain(pattern)

fun Script.matchesAnyOrNull(pattern: Script): Boolean? =
	pattern == script("any")

fun Script.matchesPlain(pattern: Script): Boolean =
	when (pattern.lineStack) {
		is EmptyStack -> lineStack is EmptyStack
		is LinkStack -> lineStack is LinkStack
			&& lineStack.link.value.matches(pattern.lineStack.link.value)
			&& lineStack.link.stack.script.matches(pattern.lineStack.link.stack.script)
	}

fun ScriptLine.matches(patternLine: ScriptLine): Boolean =
	name == patternLine.name && rhs.matches(patternLine.rhs)
