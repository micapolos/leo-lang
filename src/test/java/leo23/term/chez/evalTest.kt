package leo23.term.chez

import leo.base.assertEqualTo
import leo14.number
import leo23.term.apply
import leo23.term.argExpr
import leo23.term.does
import leo23.term.eval.eval
import leo23.term.expr
import leo23.term.ifThenElse
import leo23.term.isNil
import leo23.term.nilExpr
import leo23.term.numberMinus
import leo23.term.numberPlus
import leo23.term.params
import leo23.term.switch
import leo23.term.textAppend
import leo23.term.textNumberOrNil
import leo23.term.tuple
import leo23.term.tupleAt
import leo23.term.type.numberType
import leo23.term.type.textType
import kotlin.test.Test

class EvalTest {
	@Test
	fun textAppend() {
		expr("Hello, ")
			.textAppend(expr("world!"))
			.evalString
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun fnApply() {
		params(numberType, numberType)
			.does(argExpr(1, numberType).numberMinus(argExpr(0, numberType)))
			.apply(expr(5), expr(3))
			.evalString
			.assertEqualTo("2")
	}

	@Test
	fun vectorAt() {
		tuple(expr("Hello"), expr("world"))
			.tupleAt(0)
			.evalString
			.assertEqualTo("Hello")
	}

	@Test
	fun textNumber_number() {
		expr("10")
			.textNumberOrNil
			.switch(
				argExpr(0, numberType).numberPlus(expr(20)),
				expr(0))
			.evalString
			.assertEqualTo("30")
	}

	@Test
	fun textNumber_nil() {
		expr("10a")
			.textNumberOrNil
			.switch(
				argExpr(0, numberType).numberPlus(expr(20)),
				expr(0))
			.evalString
			.assertEqualTo("0")
	}

	@Test
	fun ifThenElse_true() {
		expr(true)
			.ifThenElse(expr(10), expr(20))
			.evalString
			.assertEqualTo("10")
	}

	@Test
	fun ifThenElse_false() {
		expr(false)
			.ifThenElse(expr(10), expr(20))
			.evalString
			.assertEqualTo("20")
	}

	@Test
	fun isNil_true() {
		nilExpr
			.isNil
			.evalString
			.assertEqualTo("#t")
	}

	@Test
	fun isNil_false() {
		expr("hello")
			.isNil
			.evalString
			.assertEqualTo("#f")
	}
}