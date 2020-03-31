package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun applyAppends() {
		function(
			scope(),
			script(givenName lineTo script()))
			.apply(thunk(value("foo" lineTo value())))
			.assertEqualTo(
				thunk(
					value(
						givenName lineTo value(
							"foo" lineTo value()))))
	}

	@Test
	fun applyResolvesContext() {
		function(
			scope(
				definition(
					thunk(
						value(
							"foo" lineTo value()))
						.bindingTo(thunk(
							value(
								"bar" lineTo value()))))),
			script("foo" lineTo script()))
			.apply(thunk(value("goo" lineTo value())))
			.assertEqualTo(thunk(value("bar")))
	}
}