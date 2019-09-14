package leo13.untyped.pattern

import leo13.script.lineTo
import leo13.script.plus
import leo13.untyped.functionName
import leo13.untyped.givingName
import leo13.untyped.value.ValueFunction

data class PatternArrow(val lhs: Pattern, val rhs: Pattern)

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.matches(function: ValueFunction): Boolean =
	this == function.patternArrow

fun PatternArrow.contains(arrow: PatternArrow) =
	this == arrow // TODO: Consider weaker version, lhs.arrow.contains(lhs) && rhs.contains(arrow.rhs)

val PatternArrow.scriptLine
	get() =
		functionName lineTo lhs.bodyScript.plus(givingName lineTo rhs.bodyScript)
