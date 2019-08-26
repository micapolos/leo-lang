package leo13

import leo.base.assertEqualTo
import leo13.script.evaluator.fn
import org.junit.Test

class ValueBuilderTest {
	@Test
	fun asScript() {
		value().assertEqualsToScript("")
		value("zero" lineTo value()).assertEqualsToScript("zero()")
		value("zero" lineTo value(), "one" lineTo value()).assertEqualsToScript("zero()one()")
		value("zero" lineTo value("one" lineTo value())).assertEqualsToScript("zero(one())")
		value("fn" lineTo value()).assertEqualsToScript("meta(fn())")

		value(fn()).assertEqualsToScript("fn(bindings(null())expr(null()))")
		value(fn(), "zero" lineTo value()).assertEqualsToScript("fn(bindings(null())expr(null()))zero()")
		value(fn(), "expr" lineTo value()).assertEqualsToScript("fn(bindings(null())expr(null()))expr()")
	}

	@Test
	fun scriptValue() {
		script().value.assertEqualTo(value())

		script(
			"zero" lineTo script(),
			"plus" lineTo script("one" lineTo script()))
			.value
			.assertEqualTo(
				value(
					"zero" lineTo value(),
					"plus" lineTo value("one" lineTo value())))
	}
}