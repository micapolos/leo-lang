package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class DefinerTest {
	@Test
	fun gives() {
		context()
			.define(
				script(
					"foo" lineTo script(),
					"gives" lineTo script("given")
				)
			)
			.assertEqualTo(
				context()
					.plus(script("foo"), binding(Function(context(), script("given"))))
			)
	}
}