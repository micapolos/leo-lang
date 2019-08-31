package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun lhsOrNull() {
		pattern()
			.previousOrNull
			.assertEqualTo(null)

		pattern("one" lineTo pattern())
			.previousOrNull
			.assertEqualTo(pattern())

		pattern("one" lineTo pattern(), "two" lineTo pattern())
			.previousOrNull
			.assertEqualTo(pattern("one" lineTo pattern()))

		pattern(unsafeChoice("one" caseTo pattern(), "two" caseTo pattern()))
			.previousOrNull
			.assertEqualTo(pattern())

		pattern(arrow(type(pattern("one")), type(pattern("two"))))
			.previousOrNull
			.assertEqualTo(pattern())
	}
}