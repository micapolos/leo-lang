package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(constant(program("foo")))
			.apply(program("arg" valueTo program()))
			.assertEqualTo(program("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		body(
			function(
				context(),
				script("given" lineTo script())))
			.apply(program(literal(5)))
			.assertEqualTo(program("given" valueTo program(literal(5))))
	}
}