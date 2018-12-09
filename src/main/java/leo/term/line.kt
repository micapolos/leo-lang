package leo.term

import leo.Word

data class Line<out V>(
	val operator: Operator,
	val rightOrNull: Right<V>?)

infix fun <V> Operator.lineTo(rightOrNull: Right<V>?): Line<V> =
	Line(this, rightOrNull)

infix fun <V> Word.lineTo(termOrNull: Term<V>?): Line<V> =
	operator.lineTo(termOrNull?.operand?.right)
