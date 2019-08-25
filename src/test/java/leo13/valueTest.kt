package leo13

import leo.base.assertEqualTo
import leo13.script.expr
import org.junit.Test

class ValueBuilderTest {
	@Test
	fun asScript() {
		value().assertEqualsToScript("")
		value("zero" lineTo value()).assertEqualsToScript("zero()")
		value("zero" lineTo value(), "one" lineTo value()).assertEqualsToScript("zero()one()")
		value("zero" lineTo value("one" lineTo value())).assertEqualsToScript("zero(one())")
		value("expr" lineTo value()).assertEqualsToScript("meta(expr())")

		value(expr()).assertEqualsToScript("expr(null())")
		value(expr(), "zero" lineTo value()).assertEqualsToScript("expr(null())zero()")
		value(expr(), "expr" lineTo value()).assertEqualsToScript("expr(null())expr()")
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