package leo13.script

import leo.base.fold
import leo13.LeoObject
import leo13.fail
import leo13.failRun

data class Switch(val lhsNode: SwitchNode, val case: Case) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = lhsNode.scriptableBody.plus(case.scriptableBody)
}

fun uncheckedSwitch(lhsNode: SwitchNode, case: Case) = Switch(lhsNode, case)

fun Switch.uncheckedPlus(case: Case): Switch =
	uncheckedSwitch(node(this), case)

fun Switch.plus(case: Case): Switch =
	caseOrNull(case.name).let { existingCase ->
		if (existingCase != null) fail("duplicate" lineTo script(existingCase.scriptableLine, case.scriptableLine))
		else uncheckedPlus(case)
	}

fun Switch.caseOrNull(name: String): Case? =
	if (case.name == name) case
	else lhsNode.caseOrNull(name)

fun switch(firstCase: Case, secondCase: Case, vararg cases: Case): Switch =
	switchNode(firstCase).plusSwitch(secondCase).fold(cases) { plus(it) }

val Script.switch: Switch
	get() =
		failRun("switch") {
			switchParser().fold(lineSeq) { push(it) }
		}.switch
