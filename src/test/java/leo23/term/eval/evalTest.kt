package leo23.term.eval

import leo.base.assertEqualTo
import leo23.term.apply
import leo23.term.fn2
import leo23.term.get
import leo23.term.indexed
import leo23.term.minus
import leo23.term.number
import leo23.term.plus
import leo23.term.switch
import leo23.term.text
import leo23.term.textAppend
import leo23.term.v0
import leo23.term.v1
import leo23.term.vector
import kotlin.test.Test

fun num(int: Int) = leo14.number(int)

class EvalTest {
	@Test
	fun vectorAt() {
		vector(text("Hello"), text("world"))
			.get(number(0))
			.eval
			.assertEqualTo("Hello")
	}

	@Test
	fun fnApply() {
		fn2(v1.minus(v0))
			.apply(number(5), number(3))
			.eval
			.assertEqualTo(num(2))
	}

	@Test
	fun switch_0() {
		0.indexed(text("10"))
			.switch(
				v0.textAppend(text("10")),
				v0.plus(number(10)))
			.eval
			.assertEqualTo("1010")
	}

	@Test
	fun switch_1() {
		1.indexed(number(10))
			.switch(
				v0.textAppend(text("10")),
				v0.plus(number(10)))
			.eval
			.assertEqualTo(num(20))
	}
}