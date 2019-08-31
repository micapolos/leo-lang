package leo13.script

import leo.base.failIfOr
import leo.base.fold
import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.type.Choice
import leo13.type.ChoiceMatch
import leo9.EmptyStack
import leo9.LinkStack

data class Switch(val lhsNode: SwitchNode, val case: Case) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = lhsNode.scriptableBody.plus(case.scriptableBody)
}

fun uncheckedSwitch(lhsNode: SwitchNode, case: Case) = Switch(lhsNode, case)

fun Switch.uncheckedPlus(case: Case): Switch =
	uncheckedSwitch(node(this), case)

fun Switch.plusOrNull(case: Case): Switch? =
	notNullIf(!containsCase(case.name)) {
		uncheckedPlus(case)
	}

fun Switch.unsafePlus(case: Case): Switch =
	plusOrNull(case) ?: error("duplicate case")

fun Switch.containsCase(name: String): Boolean =
	case.name == name || lhsNode.containsCase(name)

fun unsafeSwitch(firstCase: Case, secondCase: Case, vararg cases: Case): Switch =
	switchNode(firstCase).unsafePlusSwitch(secondCase).fold(cases) { unsafePlus(it) }

val ScriptLine.switchOrNull: Switch?
	get() =
		ifOrNull(name == "switch") {
			rhs.switchOrNull
		}

val Script.switchOrNull: Switch?
	get() =
		when (lineStack) {
			is EmptyStack -> null
			is LinkStack -> when (lineStack.link.stack) {
				is EmptyStack -> null
				is LinkStack ->
					caseOrNull(lineStack.link.stack.link.value, lineStack.link.value)?.let { case ->
						lineStack.link.stack.link.stack.script.switchNodeOrNull?.let { node ->
							node.plusSwitchOrNull(case)
						}
					}
			}
		}

val Script.switchNodeOrNull: SwitchNode?
	get() =
		when (lineStack) {
			is EmptyStack -> null
			is LinkStack -> when (lineStack.link.stack) {
				is EmptyStack -> null
				is LinkStack ->
					caseOrNull(lineStack.link.stack.link.value, lineStack.link.value)?.let { case ->
						when (lineStack.link.stack.link.stack) {
							is EmptyStack -> switchNode(case)
							is LinkStack -> switchOrNull?.let { switch -> node(switch) }
						}
					}
			}
		}

fun caseOrNull(firstScriptLine: ScriptLine, secondScriptLine: ScriptLine): Case? =
	ifOrNull(firstScriptLine.rhs.isEmpty && secondScriptLine.name == "gives") {
		firstScriptLine.name caseTo secondScriptLine.rhs
	}

val Script.unsafeSwitch: Switch
	get() =
		switchOrNull ?: error("switch parse error")

val ScriptLine.unsafeSwitch: Switch
	get() =
		failIfOr(name != "switch") { rhs.unsafeSwitch }

val String.unsafeSwitch
	get() =
		unsafeScriptLine.unsafeSwitch

fun Switch.choiceMatchOrNull(choice: Choice): ChoiceMatch? =
	TODO()
//	distinctCaseStack
//		.mapOrNull {
//			choice
//				.rhsOrNull(name)
//				?.let { either -> match(either, rhs) }
//		}
//		?.choiceMatch
//		?.orNullIf { !(choice.caseStack.drop(caseMatchStack)?.isEmpty ?: true) }