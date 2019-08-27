package leo13.type.pattern

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.script

val choiceScriptableName = "choice"

sealed class Choice : Scriptable() {
	override val scriptableName get() = leo13.type.pattern.choiceScriptableName
	abstract val choiceScriptableName: String
	abstract val choiceScriptableBody: Script
}

data class EmptyChoice(val empty: Empty) : Choice() {
	override fun toString() = super.toString()
	override val scriptableBody get() = script()
	override val choiceScriptableName get() = "empty"
	override val choiceScriptableBody get() = script()
}

data class LinkChoice(val link: ChoiceLink) : Choice() {
	override fun toString() = super.toString()
	override val scriptableBody get() = choiceScriptableBody
	override val choiceScriptableName get() = "link"
	override val choiceScriptableBody get() = link.scriptableBody
}

fun choice(empty: Empty): Choice = EmptyChoice(empty)
fun choice(link: ChoiceLink): Choice = LinkChoice(link)

fun Choice.plus(vararg cases: Case) = fold(cases) { choice(link(this, it)) }
fun choice(vararg cases: Case) = choice(empty).plus(*cases)

fun Choice.contains(pattern: Pattern): Boolean =
	when (pattern) {
		is EmptyPattern -> false
		is LinkPattern -> contains(pattern.link)
		is ChoicePattern -> contains(pattern.choice)
		is ArrowPattern -> false
	}

fun Choice.contains(patternLink: PatternLink): Boolean =
	patternLink.lhs is EmptyPattern && contains(patternLink.line)

fun Choice.contains(patternLine: PatternLine): Boolean =
	when (this) {
		is EmptyChoice -> false
		is LinkChoice -> link.contains(patternLine)
	}

// The approach for now is, that choices must match exactly.
fun Choice.contains(choice: Choice): Boolean =
	when (this) {
		is EmptyChoice -> when (choice) {
			is EmptyChoice -> true
			is LinkChoice -> false
		}
		is LinkChoice -> when (choice) {
			is EmptyChoice -> false
			is LinkChoice -> link.contains(choice.link)
		}
	}
