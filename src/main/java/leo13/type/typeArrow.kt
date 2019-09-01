package leo13.type

import leo13.LeoObject
import leo13.script.lineTo
import leo13.script.plus

data class TypeArrow(val lhs: Type, val rhs: Type) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "arrow"
	override val scriptableBody get() = lhs.scriptableBody.plus("to" lineTo rhs.scriptableBody)
}

fun arrow(lhs: Type, rhs: Type) = TypeArrow(lhs, rhs)

fun TypeArrow.contains(arrow: TypeArrow): Boolean =
	this == arrow
