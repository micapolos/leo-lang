package leo13.script

import leo.base.notNullIf
import leo13.LeoObject
import leo13.fail

sealed class SwitchNode : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "node"
	override val scriptableBody get() = nodeScriptableBody
	abstract val nodeScriptableBody: Script
}

data class CaseSwitchNode(val case: Case) : SwitchNode() {
	override fun toString() = super.toString()
	override val nodeScriptableBody get() = case.scriptableBody
}

data class SwitchSwitchNode(val switch: Switch) : SwitchNode() {
	override fun toString() = super.toString()
	override val nodeScriptableBody get() = switch.scriptableBody
}

fun switchNode(case: Case): SwitchNode = CaseSwitchNode(case)
fun node(switch: Switch): SwitchNode = SwitchSwitchNode(switch)

fun SwitchNode.caseOrNull(name: String): Case? =
	when (this) {
		is CaseSwitchNode -> notNullIf(case.name == name) { case }
		is SwitchSwitchNode -> switch.caseOrNull(name)
	}

fun SwitchNode.plus(case: Case): SwitchNode =
	node(plusSwitch(case))

fun SwitchNode.plusSwitch(case: Case): Switch =
	caseOrNull(case.name).let { existingCase ->
		if (existingCase != null) fail("duplicate" lineTo script(existingCase.scriptableLine, case.scriptableLine))
		else uncheckedPlusSwitch(case)
	}

fun SwitchNode.uncheckedPlusSwitch(case: Case): Switch =
	uncheckedSwitch(this, case)
