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

val pattern.isAny: Boolean
	get() =
		script == script("any")

fun Script.matches(pattern: pattern): Boolean =
	pattern.isAny || matchesPlain(pattern.script)

fun Script.matches(script: Script): Boolean =
	matches(pattern(script))

fun Script.matchesPlain(pattern: Script): Boolean =
	when (pattern.lineStack) {
		is EmptyStack -> lineStack is EmptyStack
		is LinkStack -> lineStack is LinkStack
			&& lineStack.link.value.matches(pattern.lineStack.link.value)
			&& lineStack.link.stack.script.matches(pattern(pattern.lineStack.link.stack.script))
	}

fun ScriptLine.matches(patternLine: ScriptLine): Boolean =
	name == patternLine.name && rhs.matches(pattern(patternLine.rhs))
