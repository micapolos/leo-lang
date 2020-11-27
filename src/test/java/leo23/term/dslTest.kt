package leo23.term

import leo23.term.type.numberType
import kotlin.test.Test

class DslTest {
	@Test
	fun dsl() {
		argExpr(0, numberType)

		nilExpr

		expr(true)
		expr(true).ifThenElse(expr("true"), expr("false"))

		expr(10)
		expr(10.0)
		expr(2).numberPlus(expr(3))
		expr(2).numberMinus(expr(3))
		expr(2).numberTimes(expr(3))
		expr(2).numberEquals(expr(3))
		expr(2).numberText

		expr("Hello")
		expr("Hello, ").textAppend(expr("world!"))
		expr("foo").textNumberOrNil

		tuple()
		tuple(expr(10), expr("foo"))
		tuple(expr(10), expr("foo")).tupleAt(0)

		params(numberType, numberType).does(argExpr(0, numberType).numberMinus(argExpr(1, numberType)))
	}
}