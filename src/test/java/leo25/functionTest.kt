package leo25

import leo.base.assertEqualTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun apply() {
		Function(context(), script("name"))
			.apply(value("name" lineTo value("foo")))
			.assertEqualTo(value("name" lineTo value("foo")))
	}
}