package leo13.value

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
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

	@Test
	fun firstLineOrNull() {
		value(
			"x" lineTo value("zero"),
			"y" lineTo value("one"),
			"x" lineTo value("two"))
			.run {
				firstLineOrNull("x").assertEqualTo("x" lineTo value("two"))
				firstLineOrNull("y").assertEqualTo("y" lineTo value("one"))
				firstLineOrNull("z").assertEqualTo(null)
			}
	}

	@Test
	fun accessOrNull() {
		value("vec" lineTo value(
			"x" lineTo value("zero"),
			"y" lineTo value("one")))
			.run {
				accessOrNull("x").assertEqualTo(value("x" lineTo value("zero")))
				accessOrNull("y").assertEqualTo(value("y" lineTo value("one")))
				accessOrNull("z").assertEqualTo(null)
			}
	}
}