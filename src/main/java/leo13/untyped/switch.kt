package leo13.untyped

import leo13.ObjectScripting
import leo13.script
import leo13.script.*
import leo13.scripting
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

val switchReader: Reader<Switch> =
	stackReader(switchName, caseReader, ::switch)

val switchWriter: Writer<Switch> =
	stackWriter(switchName, caseWriter, Switch::caseStack)

data class Switch(val caseStack: Stack<Case>) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"switch" lineTo caseStack.scripting.script
}

fun switch(cases: Stack<Case>) = Switch(cases)
fun switch(vararg cases: Case) = switch(stack(*cases))
fun Switch.plus(case: Case) = Switch(caseStack.push(case))

fun Switch.resolveCaseRhsOrNull(script: Script): Script? =
	script
		.linkOrNull
		?.line
		?.rhs
		?.onlyLineOrNull
		?.let { line -> caseRhsOrNull(line) }

fun Switch.caseRhsOrNull(line: ScriptLine): Script? =
	caseStack.mapFirst { rhsOrNull(line) }
