package leo14

import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.lambda.js.Term
import leo14.lambda.term
import java.math.BigDecimal

data class Number(val bigDecimal: BigDecimal) {
	override fun toString() = bigDecimal.toString()
}

fun number(bigDecimal: BigDecimal): Number = Number(bigDecimal)
fun number(int: Int): Number = Number(BigDecimal.valueOf(int.toLong()))
fun number(long: Long): Number = Number(BigDecimal.valueOf(long))
fun number(double: Double): Number = Number(BigDecimal.valueOf(double))

val Number.code
	get() =
		toString()

val Number.term: Term
	get() = term(expr)

val Number.expr: Expr
	get() =
		expr(bigDecimal.toDouble())

val Number.any: Any
	get() =
		bigDecimal

operator fun Number.plus(number: Number) =
	number(bigDecimal + number.bigDecimal)

operator fun Number.minus(number: Number) =
	number(bigDecimal - number.bigDecimal)

operator fun Number.times(number: Number) =
	number(bigDecimal * number.bigDecimal)

operator fun Number.unaryMinus() =
	number(-bigDecimal)
