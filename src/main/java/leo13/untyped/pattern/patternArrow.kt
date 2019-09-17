package leo13.untyped.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.plus
import leo13.untyped.functionName
import leo13.untyped.givingName
import leo13.untyped.value.ValueFunction

data class PatternArrow(val lhs: Pattern, val rhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine get() = scriptLine
}

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.matches(function: ValueFunction): Boolean =
	TODO() // Do we need pattern.matches(value) anyway?

fun PatternArrow.contains(arrow: PatternArrow) =
	this == arrow // TODO: Consider weaker version, lhs.arrow.contains(lhs) && rhs.contains(arrow.rhs)

val PatternArrow.scriptLine
	get() =
		functionName lineTo lhs.bodyScript.plus(givingName lineTo rhs.bodyScript)

fun PatternArrow.rhsOrNull(pattern: Pattern): Pattern? =
	notNullIf(lhs == pattern) { rhs }