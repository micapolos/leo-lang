package leo.term

import leo.Word

data class Line<out V>(
	val operator: Operator,
	val right: Right<V>)

infix fun <V> Operator.lineTo(right: Right<V>): Line<V> =
	Line(this, right)

infix fun <V> Word.lineTo(termOrNull: Term<V>?): Line<V> =
	operator.lineTo(termOrNull.right)
