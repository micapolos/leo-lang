package leo5

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun contains() {
		pattern()
			.contains(value())
			.assertEqualTo(true)

		pattern()
			.contains(value("one" lineTo value()))
			.assertEqualTo(false)

		pattern(dictionary("one" lineTo pattern()))
			.contains(value())
			.assertEqualTo(false)

		pattern(dictionary("one" lineTo pattern()))
			.contains(value("one" lineTo value()))
			.assertEqualTo(true)

		pattern(dictionary("one" lineTo pattern()))
			.contains(value("two" lineTo value()))
			.assertEqualTo(false)

		pattern(dictionary("one" lineTo pattern(dictionary("two" lineTo pattern()))))
			.contains(value("one" lineTo value("two" lineTo value())))
			.assertEqualTo(true)
	}
}