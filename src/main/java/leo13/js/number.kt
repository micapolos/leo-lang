package leo13.js

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