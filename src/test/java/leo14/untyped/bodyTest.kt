package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class BodyTest {
	@Test
	fun apply_nullContext() {
		body(thunk(value("foo")))
			.apply(context(), thunk(value("arg" lineTo value())))
			.assertEqualTo(applied(thunk(value("foo"))))
	}

	@Test
	fun apply_nonNullContext() {
		evalBody(script(givenName lineTo script()))
			.apply(context(), thunk(value(literal(5))))
			.assertEqualTo(applied(thunk(value(givenName lineTo value(literal(5))))))
	}
}