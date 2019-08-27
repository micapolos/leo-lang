package leo13.type.pattern

import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.plus

val arrowScriptableName = "arrow"

data class PatternArrow(val lhs: Pattern, val rhs: Pattern) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = arrowScriptableName
	override val scriptableBody get() = lhs.scriptableBody.plus("to" lineTo rhs.scriptableBody)
}

infix fun Pattern.arrowTo(rhs: Pattern) = PatternArrow(this, rhs)

fun PatternArrow.contains(arrow: PatternArrow): Boolean =
	this == arrow