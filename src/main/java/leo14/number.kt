package leo14

import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.lambda.js.Term
import leo14.lambda.term
import java.math.BigDecimal
import kotlin.math.cos
import kotlin.math.sin

data class Number(val bigDecimal: BigDecimal) {
	override fun toString() = bigDecimal.toString()
}

fun number(bigDecimal: BigDecimal): Number = Number(bigDecimal)
fun number(int: Int): Number = Number(BigDecimal.valueOf(int.toLong()))
fun number(long: Long): Number = Number(BigDecimal.valueOf(long))
fun number(double: Double): Number = Number(double.bigDecimal)

val Int.number: Number get() = number(this)
val Double.number: Number get() = number(this)

val Int.bigDecimal get() = BigDecimal.valueOf(toLong())
val Int.unsignedBigDecimal get() = BigDecimal.valueOf(toUInt().toLong())
val Double.bigDecimal get() = BigDecimal.valueOf(this).stripDotZero

val BigDecimal.stripDotZero: BigDecimal
	get() =
		try {
			toBigIntegerExact().toBigDecimal()
		} catch (e: ArithmeticException) {
			this
		}

val String.numberOrNull: Number?
	get() =
		try {
			number(BigDecimal(this))
		} catch (e: NumberFormatException) {
			null
		}

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

operator fun Number.compareTo(number: Number) =
	bigDecimal.compareTo(number.bigDecimal)

fun Number.isEqualTo(number: Number) =
	equals(number)

operator fun Number.unaryMinus() =
	number(-bigDecimal)

val Number.sinus: Number
	get() =
		sin(bigDecimal.toDouble()).number

val Number.cosinus: Number
	get() =
		cos(bigDecimal.toDouble()).number

val Number.string: String
	get() =
		toString()