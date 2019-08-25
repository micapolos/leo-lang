package leo13

import leo9.*

data class Choice(
	val eitherStack: Stack<Either>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = eitherStack.asScriptLine("choice")
}

val Stack<Either>.choice get() = Choice(this)
fun choice(vararg eithers: Either) = stack(*eithers).choice
fun Choice.plus(either: Either) = eitherStack.push(either).choice

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	eitherStack.any { matches(scriptLine) }

fun Choice.contains(type: Type): Boolean =
	if (type.choiceOrNull == null) type.onlyLineOrNull?.let { contains(it.either) } ?: false
	else type.lineStack.isEmpty && contains(type.choiceOrNull)

fun Choice.contains(choice: Choice): Boolean =
	choice.eitherStack.all { this@contains.contains(this) }

fun Choice.contains(either: Either): Boolean =
	eitherStack.any { contains(either) }

val ScriptLine.choiceOrNull: Choice?
	get() = asStackOrNull("choice") { eitherOrNull }?.choice