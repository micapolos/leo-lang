package leo13.js.compiler

import leo13.lambda.js.Value
import leo13.lambda.js.value

// TODO: Refactor to allow arbitrary large number, represented as syntax and not value
sealed class Number

data class IntNumber(val int: Int) : Number()
data class DoubleNumber(val double: Double) : Number()

fun number(int: Int): Number = IntNumber(int)
fun number(double: Double): Number = DoubleNumber(double)

val Number.code
	get() =
		when (this) {
			is IntNumber -> "$int"
			is DoubleNumber -> "$double"
		}

val Number.value: Value
	get() =
		when (this) {
			is IntNumber -> value(int)
			is DoubleNumber -> value(double)
		}