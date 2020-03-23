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
			.apply(program("foo" valueTo program()))
			.assertEqualTo(
				thunk(
					program(
						givenName valueTo program(
							"foo" valueTo program()))))
	}

	@Test
	fun applyResolvesContext() {
		function(
			context(
				rule(
					pattern(
						program(
							"foo" valueTo program())),
					body(
						thunk(
							program(
								"bar" valueTo program()))))),
			script("foo" lineTo script()))
			.apply(program("goo" valueTo program()))
			.assertEqualTo(thunk(program("bar")))
	}
}