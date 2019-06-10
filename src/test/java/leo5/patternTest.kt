package leo5

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTest {
	@Test
	fun contains() {
		pattern()
			.contains(script())
			.assertEqualTo(true)

		pattern()
			.contains(script(line("one")))
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern())))
			.contains(script())
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern())))
			.contains(script(line("one", value())))
			.assertEqualTo(true)

		pattern(dictionary(line("one", pattern())))
			.contains(script(line("two", value())))
			.assertEqualTo(false)

		pattern(dictionary(line("one", pattern(dictionary(line("two", pattern()))))))
			.contains(script(line("one", value(script(line("two"))))))
			.assertEqualTo(true)
	}
}