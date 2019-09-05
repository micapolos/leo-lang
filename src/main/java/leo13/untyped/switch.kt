package leo13.untyped

import leo13.LeoStruct
import leo13.base.List
import leo13.base.list
import leo13.base.mapFirst
import leo13.base.plus
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.onlyLineOrNull

data class switch(val cases: List<case> = list(), val elseOrNull: Script? = null) : LeoStruct("switch", cases, elseOrNull) {
	override fun toString() = super.toString()
}

fun switch.plus(case: case) = switch(cases.plus(case), elseOrNull)
fun switch.plusElse(rhs: Script) = switch(cases, rhs)

fun switch.resolveCaseRhsOrNull(script: Script): Script? =
	script
		.onlyLineOrNull
		?.rhs
		?.onlyLineOrNull
		?.let { line -> caseRhsOrNull(line) }

fun switch.caseRhsOrNull(line: ScriptLine): Script? =
	cases.mapFirst { rhsOrNull(line) } ?: elseOrNull
