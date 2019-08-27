package leo13.type.pattern

data class ChoiceLink(val lhs: Choice, val case: Case)

fun link(lhs: Choice, case: Case) = ChoiceLink(lhs, case)

fun ChoiceLink.contains(choiceLink: ChoiceLink): Boolean =
	lhs.contains(choiceLink.lhs) && case.contains(choiceLink.case)

fun ChoiceLink.contains(patternLine: PatternLine): Boolean =
	lhs.contains(patternLine) || case.contains(patternLine)
