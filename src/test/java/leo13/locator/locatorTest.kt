package leo13.locator

import leo.base.assertEqualTo
import leo13.assertFailsWith
import leo13.traced
import leo13.tracedError
import leo9.push
import leo9.stack
import kotlin.test.Test

class LocatorTest {
	@Test
	fun pushSuccess() {
		stack('a')
			.locator(location(line(2), column(4)))
			.plus('b') { push(it) }
			.assertEqualTo(
				stack('a', 'b')
					.locator(location(line(2), column(5))))
	}

	@Test
	fun pushError() {
		traced {
			stack('a')
				.locator(location(line(2), column(4)))
				.plus('b') { tracedError() }
		}.assertFailsWith(location(line(2), column(4)).scriptLine)
	}
}