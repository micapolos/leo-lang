package leo14.untyped

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class ActionTest {
	@Test
	fun applyAppends() {
		action(
			scope(),
			script(scriptName lineTo script()))
			.applyScript(thunk(value("foo" lineTo value())))
			.assertEqualTo(
				thunk(
					value(
						scriptName lineTo value(
							"foo" lineTo value()))))
	}

	@Test
	fun applyResolvesContext() {
		action(
			scope(
				definition(
					thunk(
						value(
							"foo" lineTo value()))
						.bindingTo(thunk(
							value(
								"bar" lineTo value()))))),
			script("foo" lineTo script()))
			.applyScript(thunk(value("goo" lineTo value())))
			.assertEqualTo(thunk(value("bar")))
	}
}