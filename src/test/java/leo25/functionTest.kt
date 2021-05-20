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
			.assertEqualTo(value("ok"))
	}

	@Test
	fun applyGiven() {
		Function(context(), script("given"))
			.apply(value("foo"))
			.assertEqualTo(value("given" lineTo value("foo")))
	}

	@Test
	fun applyGivenFoo() {
		Function(context(), script("given" lineTo script(), "name" lineTo script()))
			.apply(value("name" lineTo value("foo")))
			.assertEqualTo(value("name" lineTo value("foo")))
	}
}