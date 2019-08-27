package leo13.type.pattern

import leo13.script.Scriptable
import leo13.script.plus

data class ChoiceLink(val lhs: Choice, val case: Case) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = lhs.scriptableBody.plus(case.scriptableLine)
}

fun link(lhs: Choice, case: Case) = ChoiceLink(lhs, case)

fun ChoiceLink.contains(choiceLink: ChoiceLink): Boolean =
	lhs.contains(choiceLink.lhs) && case.contains(choiceLink.case)

fun ChoiceLink.contains(patternLine: PatternLine): Boolean =
	lhs.contains(patternLine) || case.contains(patternLine)
