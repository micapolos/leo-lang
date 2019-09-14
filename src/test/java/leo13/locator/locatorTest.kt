package leo13.locator

import leo.base.assertEqualTo
import leo13.*
import leo13.script.lineTo
import leo13.script.script
import leo9.stack
import kotlin.test.Test

class LocatorTest {
	@Test
	fun scripting() {
		stack('a')
			.charProcessor
			.locator()
			.scriptingLine
			.assertEqualTo(
				"locator" lineTo script(
					stack('a').processor { scriptLine }.scriptingLine,
					location().scriptingLine))
	}

	@Test
	fun pushSuccess() {
		stack('a')
			.charProcessor
			.locator(location(line(2), column(4)))
			.plus('b')
			.assertEqualTo(
				stack('a', 'b')
					.charProcessor
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