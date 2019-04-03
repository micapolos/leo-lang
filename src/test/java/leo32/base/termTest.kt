package leo32.base

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		term("zero", "bit")
			.term("and" fieldTo term("zero", "bit"))
			.term("or" fieldTo term("zero", "bit")
				.term("and" fieldTo term("one", "bit")))
			.term("gives" fieldTo term("zero", "bit"))
			.string.assertEqualTo("zero.bit.and(zero.bit).or(zero.bit.and(one.bit)).gives(zero.bit)")
	}

	@Test
	fun structString() {
		term("zero", "i32", "y")
			.term("x" fieldTo term("zero", "i32"))
			.term("center".field)
			.term("radius" fieldTo term("zero"))
			.term("circle".field)
			.string
			.assertEqualTo("circle(radius: zero, center(x: zero, y: zero))")
	}
}