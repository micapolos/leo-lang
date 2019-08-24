package leo13

import leo9.*

data class Choice(
	val firstCase: Case,
	val secondCase: Case,
	val caseStack: Stack<Case>) {
	override fun toString() = asScript.toString()
}

val Choice.asScript
	get() =
		script()
			.plus(firstCase.asFirstScriptLine)
			.plus(secondCase.asNextScriptLine)
			.fold(caseStack) { plus(it.asNextScriptLine) }

fun choice(firstCase: Case, secondCase: Case, vararg cases: Case) = Choice(firstCase, secondCase, stack(*cases))
fun Choice.plus(case: Case) = Choice(firstCase, secondCase, caseStack.push(case))

fun Choice.plusOrNull(scriptLine: ScriptLine) =
	scriptLine.nextCaseOrNull?.let { plus(it) }

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	caseStack.any { matches(scriptLine) } || secondCase.matches(scriptLine) || firstCase.matches(scriptLine)

fun Choice.contains(type: Type): Boolean =
	if (type.choiceOrNull == null)
		type.onlyLineOrNull?.let { contains(it.case) } ?: false
	else type.lineStack.isEmpty && contains(type.choiceOrNull)

fun Choice.contains(choice: Choice): Boolean =
	choice.caseStack.all { this@contains.contains(this) }
		&& contains(choice.secondCase)
		&& contains(choice.firstCase)

fun Choice.contains(case: Case): Boolean =
	caseStack.any { contains(case) } || secondCase.contains(case) || firstCase.contains(case)
