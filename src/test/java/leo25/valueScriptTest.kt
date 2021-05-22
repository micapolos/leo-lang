package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ValueScriptTest {
	@Test
	fun literal() {
		value(line(literal("Michał"))).script.assertEqualTo(script(literal("Michał")))
		value(line(literal(123))).script.assertEqualTo(script(literal(123)))
	}

	@Test
	fun natives() {
		value(line(dictionary().function(body(script("foo")))))
			.script
			.assertEqualTo(script(doingName lineTo script("foo")))
	}

	@Test
	fun struct() {
		value(
			"point" lineTo value(
				"x" lineTo value("zero"),
				"y" lineTo value("one")
			)
		)
			.script
			.assertEqualTo(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one")
					)
				)
			)
	}
}