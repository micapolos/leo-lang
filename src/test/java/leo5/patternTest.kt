package leo5

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun contains() {
		pattern()
			.contains(value(script()))
			.assertEqualTo(true)

		pattern()
			.contains(value(script(line("one"))))
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern())))
			.contains(value(script()))
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern())))
			.contains(value(script(line("one", value()))))
			.assertEqualTo(true)

		pattern(dictionary(line("one", pattern())))
			.contains(value(script(line("two", value()))))
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern(dictionary(line("two", pattern()))))))
			.contains(value(script(line("one", value(script(line("two")))))))
			.assertEqualTo(true)
	}
}