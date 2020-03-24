package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun applyAppends() {
		function(
			context(),
			script(givenName lineTo script()))
			.apply(thunk(program("foo" lineTo program())))
			.assertEqualTo(
				thunk(
					program(
						givenName lineTo program(
							"foo" lineTo program()))))
	}

	@Test
	fun applyResolvesContext() {
		function(
			context(
				rule(
					pattern(
						program(
							"foo" lineTo program())),
					body(
						thunk(
							program(
								"bar" lineTo program()))))),
			script("foo" lineTo script()))
			.apply(thunk(program("goo" lineTo program())))
			.assertEqualTo(thunk(program("bar")))
	}
}