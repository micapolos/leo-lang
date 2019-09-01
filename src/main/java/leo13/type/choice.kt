package leo13.type

import leo.base.fold
import leo13.LeoObject
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo9.push

data class Choice(val lhsNode: ChoiceNode, val case: Case) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "choice"
	override val scriptableBody get() = lhsNode.scriptableBody.plus(case.scriptableLineWithOr(true))
}

fun uncheckedChoice(lhsNode: ChoiceNode, case: Case): Choice = Choice(lhsNode, case)

fun unsafeChoice(firstCase: Case, secondCase: Case, vararg cases: Case) =
	choiceNode(firstCase).unsafePlusChoice(secondCase).fold(cases) { unsafePlus(it) }

fun Choice.unsafePlus(case: Case): Choice =
	if (rhsOrNull(case.name) != null) kotlin.error("duplicate" lineTo script("case" lineTo script(case.name)))
	else uncheckedChoice(node(this), case)

fun Choice.rhsOrNull(name: String): TypeRhs? = case.rhsOrNull(name) ?: lhsNode.rhsOrNull(name)

fun Choice.contains(type: Type): Boolean =
	when (type) {
		is EmptyType -> false
		is LinkType -> contains(type.link)
		is ChoiceType -> this == type.choice
		is ArrowType -> false
	}

fun Choice.contains(typeLink: TypeLink): Boolean =
	typeLink.lhs is EmptyType && contains(typeLink.line)

fun Choice.contains(typeLine: TypeLine): Boolean =
	case.contains(typeLine) || lhsNode.contains(typeLine)

val Choice.caseStack
	get() =
		lhsNode.caseStack.push(case)