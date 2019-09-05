package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.onlyLineOrNull

data class Switch(val cases: List<Case>, val elseOrNull: Script?) : LeoStruct("switch", cases, elseOrNull) {
	override fun toString() = super.toString()
}

fun switch(cases: List<Case> = list(), elseOrNull: Script? = null) = Switch(cases, elseOrNull)
fun Switch.plus(case: Case) = Switch(cases.plus(case), elseOrNull)
fun Switch.plusElse(rhs: Script) = Switch(cases, rhs)

fun Switch.resolveCaseRhsOrNull(script: Script): Script? =
	script
		.onlyLineOrNull
		?.rhs
		?.onlyLineOrNull
		?.let { line -> caseRhsOrNull(line) }

fun Switch.caseRhsOrNull(line: ScriptLine): Script? =
	cases.mapFirst { rhsOrNull(line) } ?: elseOrNull
