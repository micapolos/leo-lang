package leo13

import leo.base.ifOrNull

data class Case(val name: String, val type: Type) {
	override fun toString() = asFirstScriptLine.toString()
}

infix fun String.caseTo(type: Type) = Case(this, type)

val Case.asFirstScriptLine get() = name lineTo type.asScript
val Case.asNextScriptLine get() = "or" lineTo script(asFirstScriptLine)

val TypeLine.firstCase
	get() =
		name caseTo rhs

val ScriptLine.firstCase
	get() =
		name caseTo rhs.type

val ScriptLine.nextCaseOrNull: Case?
	get() =
		ifOrNull(name == "or") {
			rhs.onlyLineOrNull?.firstCase
		}

fun Case.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && type.matches(scriptLine.rhs)

fun Case.contains(case: Case) =
	name == case.name && type.contains(case.type)
