package leo13.untyped

import leo13.base.stackReader
import leo13.base.stackWriter
import leo13.script.*
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

val switchName = "switch"

val switchReader: Reader<Switch> =
	reader(switchName, stackReader(caseReader), ::Switch)

val switchWriter: Writer<Switch> =
	writer(switchName, field(stackWriter(caseWriter)) { caseStack })

data class Switch(val caseStack: Stack<Case>) {
	override fun toString() = super.toString()
}

fun switch(cases: Stack<Case>) = Switch(cases)
fun switch(vararg cases: Case) = switch(stack(*cases))
fun Switch.plus(case: Case) = Switch(caseStack.push(case))

fun Switch.resolveCaseRhsOrNull(script: Script): Script? =
	script
		.onlyLineOrNull
		?.rhs
		?.onlyLineOrNull
		?.let { line -> caseRhsOrNull(line) }

fun Switch.caseRhsOrNull(line: ScriptLine): Script? =
	caseStack.mapFirst { rhsOrNull(line) }
