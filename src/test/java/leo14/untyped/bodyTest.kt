package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(thunk(program("foo")))
			.apply(context(), thunk(program("arg" lineTo program())))
			.assertEqualTo(thunk(program("foo")))
	}

	@Test
	fun apply_nonNullContext() {
		body(script(givenName lineTo script()))
			.apply(context(), thunk(program(literal(5))))
			.assertEqualTo(thunk(program(givenName lineTo program(literal(5)))))
	}
}