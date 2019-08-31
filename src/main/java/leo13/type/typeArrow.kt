package leo13.type

import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.plus

data class TypeArrow(val lhs: Pattern, val rhs: Pattern) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "arrow"
	override val scriptableBody get() = lhs.scriptableBody.plus("to" lineTo rhs.scriptableBody)
}

fun arrow(lhs: Pattern, rhs: Pattern) = TypeArrow(lhs, rhs)

fun TypeArrow.contains(arrow: TypeArrow): Boolean =
	this == arrow
