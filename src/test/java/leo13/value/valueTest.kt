package leo13.value

import leo.base.assertEqualTo
import leo13.lineTo
import leo13.script
import org.junit.Test

class ValueBuilderTest {
	@Test
	fun constructors() {
		value()

		value("zero" lineTo value())
		value("zero" lineTo value(), "one" lineTo value())
		value("zero" lineTo value("one" lineTo value()))
		value("fn" lineTo value())

		value(fn())
		value(fn(), "zero" lineTo value())
		value(fn(), "expr" lineTo value())
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