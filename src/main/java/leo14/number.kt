package leo14

import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.lambda.js.Term
import leo14.lambda.term
import kotlin.math.roundToLong

// TODO: Refactor to allow arbitrary large number, represented as syntax and not value
data class Number(val double: Double) {
	override fun toString() =
		if (double.roundToLong().toDouble() == double) "${double.roundToLong()}"
		else "$double"
}

fun number(int: Int): Number = Number(int.toDouble())
fun number(double: Double): Number = Number(double)

val Number.code
	get() =
		toString()

val Number.term: Term
	get() = term(expr)

val Number.expr: Expr
	get() =
		expr(double)

val Number.any: Any
	get() =
		double
