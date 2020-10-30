package leo20

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun native() {
		value(NativeLine(10)).script.assertEqualTo(script("native" lineTo script("10")))
	}

	@Test
	fun number() {
		value(line(10)).script.assertEqualTo(script(literal(10)))
	}

	@Test
	fun text() {
		value(line("foo")).script.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun struct() {
		value(
			"x" lineTo value(line(10)),
			"y" lineTo value(line(20)))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20))))
	}

	@Test
	fun function() {
		value(line(emptyScope.function(body(script("foo")))))
			.script
			.assertEqualTo(script("function" lineTo script("foo")))
	}
}