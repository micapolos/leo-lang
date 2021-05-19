package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import kotlin.test.Test

class FunctionTest {
	@Test
	fun apply() {
		Function(context(), script("ok"))
			.apply(value("foo"))
			.assertEqualTo(value("ok" to null))
	}

	@Test
	fun applyGiven() {
		Function(context(), script("given"))
			.apply(value("foo"))
			.assertEqualTo(value("given" to value("foo")))
	}

	@Test
	fun applyGivenFoo() {
		Function(context(), script("given" lineTo script(), "name" lineTo script()))
			.apply(value("name" to value("Michał")))
			.assertEqualTo(value("name" to value("Michał")))
	}
}