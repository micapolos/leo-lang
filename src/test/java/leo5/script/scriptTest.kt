package leo5.script

import leo.base.assertEqualTo
import kotlin.test.Test

class ScriptTest {
	@Test
	fun reverseLines() {
		script(
			"x" lineTo script(
				"zero" lineTo script(),
				"one" lineTo script()),
			"y" lineTo script(
				"three" lineTo script(),
				"four" lineTo script()))
			.reverseLines
			.assertEqualTo(
				script(
					"y" lineTo script(
						"three" lineTo script(),
						"four" lineTo script()),
					"x" lineTo script(
						"zero" lineTo script(),
						"one" lineTo script())))
	}

	@Test
	fun wrap() {
		script(
			"bit" lineTo script("zero"),
			"bit" lineTo script("one"),
			"negate" lineTo script())
			.wrap("and", 2)
			.assertEqualTo(
				script(
					"bit" lineTo script("zero"),
					"and" lineTo script(
						"bit" lineTo script("one"),
						"negate" lineTo script())))
	}
}
