package leo23.term.eval

import leo.base.assertEqualTo
import leo14.number
import leo23.term.apply
import leo23.term.argExpr
import leo23.term.cast
import leo23.term.does
import leo23.term.doesRecursively
import leo23.term.expr
import leo23.term.ifThenElse
import leo23.term.numberEquals
import leo23.term.numberMinus
import leo23.term.numberPlus
import leo23.term.numberText
import leo23.term.params
import leo23.term.switch
import leo23.term.textAppend
import leo23.term.tuple
import leo23.term.tupleAt
import leo23.term.type.ChoiceType
import leo23.term.type.doesRecursively
import leo23.term.type.numberType
import leo23.term.type.textType
import kotlin.test.Test

class EvalTest {
	@Test
	fun tupleAt() {
		tuple(expr("Hello"), expr("world"))
			.tupleAt(0)
			.eval
			.assertEqualTo("Hello")
	}

	@Test
	fun fnApply() {
		params(numberType, numberType)
			.does(argExpr(1, numberType).numberMinus(argExpr(0, numberType)))
			.apply(expr(5), expr(3))
			.eval
			.assertEqualTo(2.number)
	}

	@Test
	fun recursiveFnApply() {
		params(numberType)
			.doesRecursively(
				argExpr(0, numberType)
					.numberEquals(expr(0))
					.ifThenElse(
						expr(0),
						argExpr(1, params(numberType).doesRecursively(numberType))
							.apply(
								argExpr(0, numberType).numberMinus(expr(1)))))
			.apply(expr(5))
			.eval
			.assertEqualTo(0.number)
	}

	@Test
	fun switch_0() {
		expr("10")
			.cast(ChoiceType(listOf(textType, numberType)))
			.switch(
				expr("'").textAppend(argExpr(0, textType)).textAppend(expr("'")),
				argExpr(0, numberType).numberText)
			.eval
			.assertEqualTo("'10'")
	}

	@Test
	fun switch_1() {
		expr(10)
			.cast(ChoiceType(listOf(textType, numberType)))
			.switch(
				expr("'").textAppend(argExpr(0, textType)).textAppend(expr("'")),
				argExpr(0, numberType).numberText)
			.eval
			.assertEqualTo("10")
	}
}