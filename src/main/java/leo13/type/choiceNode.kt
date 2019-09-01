package leo13.type

import leo13.Scriptable
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script
import leo9.Stack
import leo9.stack

sealed class ChoiceNode : Scriptable() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "node"
	override val scriptableBody get() = nodeScriptableBody
	abstract val nodeScriptableName: String
	abstract val nodeScriptableBody: Script
}

data class CaseChoiceNode(val case: Case) : ChoiceNode() {
	override fun toString() = super.toString()
	override val nodeScriptableName get() = "case"
	override val nodeScriptableBody get() = script(case.scriptableLine)
}

data class ChoiceChoiceNode(val choice: Choice) : ChoiceNode() {
	override fun toString() = super.toString()
	override val nodeScriptableName get() = choice.scriptableName
	override val nodeScriptableBody get() = choice.scriptableBody
}

fun choiceNode(case: Case): ChoiceNode = CaseChoiceNode(case)
fun node(choice: Choice): ChoiceNode = ChoiceChoiceNode(choice)

fun ChoiceNode.unsafePlusChoice(case: Case): Choice =
	if (rhsOrNull(case.name) != null) error("duplicate" lineTo script("case" lineTo script(case.name)))
	else uncheckedChoice(this, case)

fun ChoiceNode.rhsOrNull(name: String): PatternRhs? = when (this) {
	is CaseChoiceNode -> case.rhsOrNull(name)
	is ChoiceChoiceNode -> choice.rhsOrNull(name)
}

fun ChoiceNode.contains(patternLine: PatternLine): Boolean =
	when (this) {
		is CaseChoiceNode -> case.contains(patternLine)
		is ChoiceChoiceNode -> choice.contains(patternLine)
	}

val ChoiceNode.caseStack: Stack<Case>
	get() =
		when (this) {
			is CaseChoiceNode -> stack(case)
			is ChoiceChoiceNode -> choice.caseStack
		}
