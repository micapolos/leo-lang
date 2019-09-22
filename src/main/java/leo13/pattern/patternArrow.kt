package leo13.pattern

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.arrowName
import leo13.givesName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus

data class PatternArrow(val lhs: Pattern, val rhs: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = arrowName lineTo
			lhs.scriptingLine.rhs.plus(givesName lineTo rhs.scriptingLine.rhs)
}

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.contains(arrow: PatternArrow) =
	this == arrow // TODO: Consider weaker version, lhs.arrow.contains(lhs) && rhs.contains(arrow.rhs)

fun PatternArrow.rhsOrNull(pattern: Pattern): Pattern? =
	notNullIf(lhs == pattern) { rhs }