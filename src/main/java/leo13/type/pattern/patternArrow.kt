package leo13.type.pattern

data class PatternArrow(val lhs: Pattern, val rhs: Pattern)

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.contains(arrow: PatternArrow): Boolean =
	arrow.lhs.contains(lhs) && rhs.contains(arrow.rhs)