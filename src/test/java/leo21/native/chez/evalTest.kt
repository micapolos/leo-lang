package leo21.native.chez

import leo.base.assertEqualTo
import leo21.native.apply
import leo21.native.boolean
import leo21.native.fn2
import leo21.native.get
import leo21.native.ifThenElse
import leo21.native.isNil
import leo21.native.lhs
import leo21.native.minus
import leo21.native.nil
import leo21.native.number
import leo21.native.pairTo
import leo21.native.rhs
import leo21.native.text
import leo21.native.textAppend
import leo21.native.textNumberOrNil
import leo21.native.v0
import leo21.native.v1
import leo21.native.vector
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