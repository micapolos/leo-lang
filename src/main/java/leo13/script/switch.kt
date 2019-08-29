package leo13.script

import leo.base.failIfOr
import leo.base.fold
import leo13.type.Choice
import leo13.type.ChoiceMatch
import leo9.EmptyStack
import leo9.LinkStack

data class Switch(val lhsNode: SwitchNode, val case: Case) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = lhsNode.scriptableBody.plus(case.scriptableLine)
}

fun uncheckedSwitch(lhsNode: SwitchNode, case: Case) = Switch(lhsNode, case)

fun Switch.uncheckedPlus(case: Case): Switch =
	uncheckedSwitch(node(this), case)

fun Switch.unsafePlus(case: Case): Switch =
	if (containsCase(case.name)) error("duplicate case")
	else uncheckedPlus(case)

fun Switch.containsCase(name: String): Boolean =
	case.name == name || lhsNode.containsCase(name)

fun unsafeSwitch(firstCase: Case, secondCase: Case, vararg cases: Case): Switch =
	switchNode(firstCase).unsafePlusSwitch(secondCase).fold(cases) { unsafePlus(it) }

val Script.unsafeSwitch: Switch
	get() =
		when (lineStack) {
			is EmptyStack -> error("empty switch")
			is LinkStack -> when (lineStack.link.stack) {
				is EmptyStack -> error("single case switch")
				is LinkStack -> when (lineStack.link.stack.link.stack) {
					is EmptyStack -> unsafeSwitch(lineStack.link.stack.link.value.case, lineStack.link.value.case)
					is LinkStack -> lineStack.link.stack.script.unsafeSwitch.unsafePlus(lineStack.link.value.case)
				}
			}
		}

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