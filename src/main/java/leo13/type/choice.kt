package leo13.type

import leo.base.notNullIf
import leo.base.orNull
import leo13.script.*
import leo13.script.ScriptLine
import leo9.*

data class Choice(
	val distinctEitherStack: Stack<Either>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "choice"
	override val scriptableBody get() = distinctEitherStack.asScript
}

val Stack<Either>.uncheckedChoice get() = Choice(this)

val Stack<Either>.choiceOrNull
	get() =
		choice().orNull.fold(reverse) { this?.plusOrNull(it) }

fun choice(): Choice = stack<Either>().uncheckedChoice
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

fun Choice.contains(name: String): Boolean =
	distinctEitherStack.any { this.name == name }

fun Choice.eitherOrNull(name: String) =
	distinctEitherStack.mapFirst { notNullIf(this.name == name) { this } }

val ScriptLine.choiceOrNull: Choice?
	get() = asStackOrNull("choice") { eitherOrNull }?.choiceOrNull

fun Choice.matches(switch: Switch): Boolean =
	true
		.fold(switch.distinctCaseStack) { case ->
			and(contains(case.name))
		}
