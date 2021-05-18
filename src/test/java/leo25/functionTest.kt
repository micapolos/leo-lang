package leo25

import leo.base.assertEqualTo
import kotlin.test.Test

class FunctionTest {
	@Test
	fun apply() {
		Function(context(), body(value("ok")))
			.apply(value("foo"))
			.assertEqualTo(value("ok"))
	}

	@Test
	fun applyGiven() {
		Function(context(), body(value("given" to null)))
			.apply(value("foo"))
			.assertEqualTo(value("given" to value("foo")))
	}
}