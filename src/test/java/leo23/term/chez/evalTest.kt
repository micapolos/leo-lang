package leo23.term.chez

import leo.base.assertEqualTo
import leo23.term.apply
import leo23.term.boolean
import leo23.term.fn2
import leo23.term.get
import leo23.term.ifThenElse
import leo23.term.isNil
import leo23.term.lhs
import leo23.term.minus
import leo23.term.nil
import leo23.term.number
import leo23.term.pairTo
import leo23.term.rhs
import leo23.term.text
import leo23.term.textAppend
import leo23.term.textNumberOrNil
import leo23.term.v0
import leo23.term.v1
import leo23.term.vector
import kotlin.test.Test

class EvalTest {
	@Test
	fun textAppend() {
		text("Hello, ")
			.textAppend(text("world!"))
			.evalString
			.assertEqualTo("Hello, world!")
	}

	@Test
	fun fnApply() {
		fn2(v1 - v0)
			.apply(number(5), number(3))
			.evalString
			.assertEqualTo("2")
	}

	@Test
	fun vectorAt() {
		vector(text("Hello"), text("world"))
			.get(number(0))
			.evalString
			.assertEqualTo("Hello")
	}

	@Test
	fun pairLhs() {
		number(10)
			.pairTo(number(20))
			.lhs
			.evalString
			.assertEqualTo("10")
	}

	@Test
	fun pairRhs() {
		number(10)
			.pairTo(number(20))
			.rhs
			.evalString
			.assertEqualTo("20")
	}

	@Test
	fun textNumber_number() {
		text("123")
			.textNumberOrNil
			.evalString
			.assertEqualTo("123")
	}

	@Test
	fun textNumber_nil() {
		text("123a")
			.textNumberOrNil
			.evalString
			.assertEqualTo("()")
	}

	@Test
	fun ifThenElse_true() {
		boolean(true)
			.ifThenElse(number(10), number(20))
			.evalString
			.assertEqualTo("10")
	}

	@Test
	fun ifThenElse_false() {
		boolean(false)
			.ifThenElse(number(10), number(20))
			.evalString
			.assertEqualTo("20")
	}

	@Test
	fun isNil_true() {
		nil
			.isNil
			.evalString
			.assertEqualTo("#t")
	}

	@Test
	fun isNil_false() {
		text("hello")
			.isNil
			.evalString
			.assertEqualTo("#f")
	}
}