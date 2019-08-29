package leo13.script

import leo.base.ifOrNull
import leo.base.notNullOrError

data class Case(val name: String, val rhs: Script) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName = name
	override val scriptableBody = rhs
}

infix fun String.caseTo(rhs: Script) = Case(this, rhs)

val ScriptLine.case: Case
	get() = name caseTo rhs

val ScriptLine.caseOrNull: Case?
	get() = ifOrNull(name == "case") {
		rhs.onlyLineOrNull?.case
	}

val String.unsafeCase
	get() =
		unsafeScriptLine
			.caseOrNull
			.notNullOrError("case")
