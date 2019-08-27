package leo13.type.pattern

data class Case(val name: String, val rhs: Pattern)

infix fun String.caseTo(rhs: Pattern) = Case(this, rhs)

fun Case.contains(patternLine: PatternLine): Boolean =
	name == patternLine.name && rhs.contains(patternLine.rhs)

fun Case.contains(case: Case): Boolean =
	name == case.name && rhs.contains(case.rhs)