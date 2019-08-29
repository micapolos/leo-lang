package leo13.script

sealed class SwitchNode : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "node"
	override val scriptableBody get() = nodeScriptableBody
	abstract val nodeScriptableBody: Script
}

data class CaseSwitchNode(val case: Case) : SwitchNode() {
	override fun toString() = super.toString()
	override val nodeScriptableBody get() = script(case.scriptableLine)
}

data class SwitchSwitchNode(val switch: Switch) : SwitchNode() {
	override fun toString() = super.toString()
	override val nodeScriptableBody get() = switch.scriptableBody
}

fun switchNode(case: Case): SwitchNode = CaseSwitchNode(case)
fun node(switch: Switch): SwitchNode = SwitchSwitchNode(switch)

fun SwitchNode.containsCase(name: String): Boolean =
	when (this) {
		is CaseSwitchNode -> case.name == name
		is SwitchSwitchNode -> switch.containsCase(name)
	}

fun SwitchNode.unsafePlusSwitch(case: Case): Switch =
	if (containsCase(case.name)) error("duplicate case")
	else uncheckedPlusSwitch(case)

fun SwitchNode.uncheckedPlusSwitch(case: Case): Switch =
	uncheckedSwitch(this, case)
