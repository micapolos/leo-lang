package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(program("foo"))
			.apply(context(), program("arg" valueTo program()))
			.assertEqualTo(program("foo"))
	}

	@Test
	fun apply_nonNullContext() {
		body(script(givenName lineTo script()))
			.apply(context(), program(literal(5)))
			.assertEqualTo(program(givenName valueTo program(literal(5))))
	}
}