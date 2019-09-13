package leo13.untyped

import leo13.untyped.value.ValueFunction

data class PatternArrow(val lhs: Pattern, val rhs: Pattern)

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.matches(function: ValueFunction): Boolean =
	this == function.patternArrow