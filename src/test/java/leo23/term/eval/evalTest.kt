package leo23.term.eval

import leo.base.assertEqualTo
import leo23.term.apply
import leo23.term.fn2
import leo23.term.get
import leo23.term.minus
import leo23.term.number
import leo23.term.text
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
}