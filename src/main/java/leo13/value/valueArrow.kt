package leo13.value

import leo.base.notNullIf
import leo13.LeoObject
import leo13.script.script

data class ValueArrow(val lhs: Value, val rhs: Value) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "arrow"
	override val scriptableBody get() = script(lhs.scriptableLine, rhs.scriptableLine)
}

fun arrow(lhs: Value, rhs: Value): ValueArrow = ValueArrow(lhs, rhs)
fun ValueArrow.at(value: Value): Value? = notNullIf(lhs == value) { rhs }
