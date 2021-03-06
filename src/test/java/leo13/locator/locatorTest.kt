package leo13.locator

import leo.base.assertEqualTo
import leo13.*
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class LocatorTest {
	@Test
	fun scripting() {
		stack('a')
			.charPushProcessor
			.locator()
			.scriptingLine
			.assertEqualTo(
				"locator" lineTo script(
					stack('a').pushProcessor { scriptLine }.scriptingLine,
					location().scriptingLine))
	}

	@Test
	fun pushSuccess() {
		stack('a')
			.charPushProcessor
			.locator(location(line(2), column(4)))
			.plus('b')
			.assertEqualTo(
				stack('a', 'b')
					.charPushProcessor
					.locator(location(line(2), column(5))))
	}

	@Test
	fun pushError() {
		traced {
			errorProcessor<Char>()
				.locator(location(line(2), column(4)))
				.plus('b')
		}.assertFailsWith(location(line(2), column(4)).scriptingLine)
	}
}