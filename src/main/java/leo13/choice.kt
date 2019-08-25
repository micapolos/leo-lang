package leo13

import leo.base.notNullIf
import leo.base.orNull
import leo9.*

data class Choice(
	val distinctEitherStack: Stack<Either>) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine get() = distinctEitherStack.asScriptLine("choice")
}

val Stack<Either>.uncheckedChoice get() = Choice(this)

val Stack<Either>.choiceOrNull
	get() =
		stack<Either>().uncheckedChoice.orNull.fold(reverse) { this?.plusOrNull(it) }

fun choiceOrNull(vararg eithers: Either): Choice? = stack(*eithers).choiceOrNull
fun unsafeChoice(vararg eithers: Either): Choice = choiceOrNull(*eithers)!!

fun Choice.plusOrNull(either: Either) =
	notNullIf(distinctEitherStack.all { name != either.name }) {
		distinctEitherStack.push(either).uncheckedChoice
	}

fun Choice.matches(scriptLine: ScriptLine): Boolean =
	distinctEitherStack.any { matches(scriptLine) }

fun Choice.contains(type: Type): Boolean =
	if (type.choiceOrNull == null) type.onlyLineOrNull?.let { contains(it.either) } ?: false
	else type.lineStack.isEmpty && contains(type.choiceOrNull)

fun Choice.contains(choice: Choice): Boolean =
	choice.distinctEitherStack.all { this@contains.contains(this) }

fun Choice.contains(either: Either): Boolean =
	distinctEitherStack.any { contains(either) }

val ScriptLine.choiceOrNull: Choice?
	get() = asStackOrNull("choice") { eitherOrNull }?.choiceOrNull
