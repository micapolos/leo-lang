package leo13.type

import leo.base.notNullIf
import leo13.script.*

data class Case(val name: String, val rhs: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithOr(withOr: Boolean): ScriptLine =
		if (withOr) "or" lineTo script(scriptableLine)
		else scriptableLine
}

infix fun String.caseTo(rhs: Type) = Case(this, rhs)

fun Case.rhsOrNull(name: String): Type? = notNullIf(this.name == name) { rhs }

val TypeLine.case: Case get() = name caseTo rhs

fun Case.contains(typeLine: TypeLine): Boolean =
	name == typeLine.name && rhs.contains(typeLine.rhs)

val Script.unsafeCase: Case
	get() =
		onlyLineOrNull!!.unsafeCase

val ScriptLine.unsafeCase: Case
	get() =
		name caseTo rhs.unsafeType

