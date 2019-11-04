package leo14

import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo14.lambda.js.Term
import leo14.lambda.term

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

val Number.term: Term
	get() = term(expr)

val Number.expr: Expr
	get() =
		when (this) {
			is IntNumber -> expr(int)
			is DoubleNumber -> expr(double)
		}

val Number.any: Any
	get() =
		when (this) {
			is IntNumber -> int
			is DoubleNumber -> double
		}
