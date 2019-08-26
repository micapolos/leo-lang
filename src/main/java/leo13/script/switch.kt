package leo13.script

import leo.base.notNullIf
import leo.base.notNullOrError
import leo.base.orNull
import leo.base.orNullIf
import leo13.type.*
import leo9.*

data class Switch(val distinctCaseStack: Stack<Case>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "switch"
	override val scriptableBody get() = distinctCaseStack.asScript
}

val Stack<Case>.uncheckedSwitch: Switch get() = Switch(this)

val Stack<Case>.switchOrNull: Switch?
	get() =
		stack<Case>().uncheckedSwitch.orNull.fold(reverse) { this?.plusOrNull(it) }

fun Switch.plusOrNull(case: Case): Switch? =
	notNullIf(!containsCase(case.name)) {
		distinctCaseStack.push(case).uncheckedSwitch
	}

fun Switch.containsCase(name: String): Boolean =
	distinctCaseStack.mapFirst { orNullIf(this.name != name) } != null

fun switchOrNull(vararg cases: Case): Switch? = stack(*cases).switchOrNull

val ScriptLine.switchOrNull: Switch?
	get() = asStackOrNull("switch") { caseOrNull }?.switchOrNull

val String.unsafeSwitch
	get() =
		unsafeScriptLine
			.switchOrNull
			.notNullOrError("switch")

fun Switch.choiceMatchOrNull(choice: Choice): ChoiceMatch? =
	distinctCaseStack
		.mapOrNull {
			choice
				.eitherOrNull(name)
				?.let { either -> match(either, rhs) }
		}
		?.choiceMatch
		?.orNullIf { !(choice.distinctEitherStack.drop(eitherMatchStack)?.isEmpty ?: true) }