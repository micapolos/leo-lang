package leo13

import leo.base.ifOrNull
import leo.base.notNullOrError

data class Case(val name: String, val rhs: Script) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName = "case"
	override val scriptableBody = script(name lineTo rhs)
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
