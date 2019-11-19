package leo14

import leo.base.assertEqualTo
import kotlin.test.Test

class SpacedStringTest {
	@Test
	fun spacedString() {
		script().spacedString.assertEqualTo("")
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.2)),
				"y" lineTo script(literal(13.5))),
			"called" lineTo script(literal("my point")))
			.spacedString
			.assertEqualTo("point x 10.2 y 13.5  called \"my point\" ")
	}
}