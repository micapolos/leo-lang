package leo16

import leo.base.notNullIf
import leo16.names.*

data class Constant(val key: Value, val value: Value) {
	override fun toString() = asValue.toString()
	val asValue get() = key.plus(_is(value))
	fun apply(arg: Value): Value? = notNullIf(key == arg) { value }
}

infix fun Value.is_(value: Value) = Constant(this, value)

val Value.isConstantOrNull: Constant?
	get() =
		matchInfix(_is) { lhs, rhs ->
			lhs.is_(rhs)
		}

val Value.hasConstantOrNull: Constant?
	get() =
		matchInfix(_has) { lhs, rhs ->
			lhs.onlyFieldOrNull?.sentenceOrNull?.word?.let { word ->
				lhs.is_(word(rhs).value)
			}
		}
