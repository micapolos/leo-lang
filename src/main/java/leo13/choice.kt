package leo13

import leo9.*

data class Choice(
	val firstEither: Either,
	val secondEither: Either,
	val remainingEitherStack: Stack<Either>) {
	override fun toString() = asScript.toString()
}

val Choice.asScript
	get() =
		script()
			.plus(firstEither.asFirstScriptLine)
			.plus(secondEither.asNextScriptLine)
			.fold(remainingEitherStack) { plus(it.asNextScriptLine) }

fun choice(firstEither: Either, secondEither: Either, vararg eithers: Either) = Choice(firstEither, secondEither, stack(*eithers))
fun Choice.plus(either: Either) = Choice(firstEither, secondEither, remainingEitherStack.push(either))

fun Choice.plusOrNull(scriptLine: ScriptLine) =
	scriptLine.nextEitherOrNull?.let { plus(it) }

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	caseStack.any { matches(scriptLine) }

fun Choice.contains(type: Type): Boolean =
	if (type.choiceOrNull == null)
		type.onlyLineOrNull?.let { contains(it.either) } ?: false
	else type.lineStack.isEmpty && contains(type.choiceOrNull)

fun Choice.contains(choice: Choice): Boolean =
	choice.caseStack.all { this@contains.contains(this) }

fun Choice.contains(either: Either): Boolean =
	caseStack.any { contains(either) }

val Choice.caseStack
	get() =
		stack(firstEither, secondEither).fold(remainingEitherStack.reverse) { push(it) }