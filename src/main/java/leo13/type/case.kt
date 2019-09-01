package leo13.type

import leo.base.notNullIf
import leo13.Scriptable
import leo13.script.*

data class Case(val name: String, val rhs: PatternRhs) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithOr(withOr: Boolean): ScriptLine =
		if (withOr) "or" lineTo script(scriptableLine)
		else scriptableLine
}

infix fun String.caseTo(rhs: PatternRhs) = Case(this, rhs)
infix fun String.caseTo(rhs: Pattern) = caseTo(thunk(rhs))

fun Case.rhsOrNull(name: String): PatternRhs? = notNullIf(this.name == name) { rhs }

val PatternLine.case: Case get() = name caseTo rhs

fun Case.contains(patternLine: PatternLine): Boolean =
	name == patternLine.name && rhs.contains(patternLine.rhs)

val Script.unsafeCase: Case
	get() =
		onlyLineOrNull!!.unsafeCase

val ScriptLine.unsafeCase: Case
	get() =
		name caseTo rhs.unsafePattern

