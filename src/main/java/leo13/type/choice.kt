package leo13.type

import leo.base.fold
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo9.push

data class Choice(val lhsNode: ChoiceNode, val case: Case) : Scriptable() {
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

fun Choice.rhsOrNull(name: String): Type? = case.rhsOrNull(name) ?: lhsNode.rhsOrNull(name)

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

//
//val Stack<Either>.uncheckedChoice get() = Choice(this)
//
//val Stack<Either>.choiceOrNull
//	get() =
//		choice().orNull.fold(reverse) { this?.plusOrNull(it) }
//
//fun choice(): Choice = stack<Either>().uncheckedChoice
//fun choiceOrNull(vararg eithers: Either): Choice? = stack(*eithers).choiceOrNull
//fun unsafeChoice(vararg eithers: Either): Choice = choiceOrNull(*eithers)!!
//
//fun Choice.plusOrNull(either: Either) =
//	notNullIf(distinctEitherStack.all { name != either.name }) {
//		distinctEitherStack.push(either).uncheckedChoice
//	}
//
//fun Choice.matches(scriptLine: ScriptLine): Boolean =
//	distinctEitherStack.any { matches(scriptLine) }
//
//fun Choice.contains(type: Type): Boolean =
//	if (type.choiceOrNull == null) type.onlyLineOrNull?.let { contains(it) } ?: false
//	else type.lineStack.isEmpty && contains(type.choiceOrNull)
//
//fun Choice.contains(choice: Choice): Boolean =
//	this == choice
//
//fun Choice.contains(line: TypeLine): Boolean =
//	distinctEitherStack.any { contains(line) }
//
//fun Choice.contains(name: String): Boolean =
//	distinctEitherStack.any { this.name == name }
//
//fun Choice.eitherOrNull(name: String) =
//	distinctEitherStack.mapFirst { notNullIf(this.name == name) { this } }
//
//val ScriptLine.choiceOrNull: Choice?
//	get() = asStackOrNull("choice") { eitherOrNull }?.choiceOrNull
//
//fun Choice.matches(switch: Switch): Boolean =
//	true
//		.fold(switch.distinctCaseStack) { case ->
//			and(contains(case.name))
//		}

val Choice.caseStack
	get() =
		lhsNode.caseStack.push(case)