package leo16

import leo16.names.*

data class Constant(val pattern: Pattern, val value: Value) {
	override fun toString() = asValue.toString()
	val asValue get() = pattern.asValue.plus(_is(value))
	fun apply(arg: Value): Value? = pattern.matchOrNull(arg)?.let { value }
}

infix fun Pattern.is_(value: Value) = Constant(this, value)

val Value.isConstantOrNull: Constant?
	get() =
		matchInfix(_is) { lhs, rhs ->
			lhs.pattern.is_(rhs)
		}

val Value.hasConstantOrNull: Constant?
	get() =
		matchInfix(_has) { lhs, rhs ->
			lhs.onlyFieldOrNull?.sentenceOrNull?.word?.let { word ->
				lhs.pattern.is_(word(rhs).value)
			}
		}
