package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(program("foo"))
			.apply(program("arg" valueTo program()))
			.assertEqualTo(program("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		body(function(context(), program("minus" valueTo program(literal(3)))))
			.apply(program(literal(5)))
			.assertEqualTo(program(literal(2)))
	}
}