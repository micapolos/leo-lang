package leo20

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun number() {
		value(10).script.assertEqualTo(script(literal(10)))
	}

	@Test
	fun text() {
		value("foo").script.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun struct() {
		value(
			"x" lineTo value(10),
			"y" lineTo value(20))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10)),
					"y" lineTo script(literal(20))))
	}

	@Test
	fun function() {
		value(line(emptyDictionary.function(body(script("foo")))))
			.script
			.assertEqualTo(script("function" lineTo script("foo")))
	}
}