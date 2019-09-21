package leo13.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class PatternTest {
	@Test
	fun scriptLine() {
		pattern()
			.scriptingLine
			.assertEqualTo("pattern" lineTo script())

		pattern("zero")
			.scriptingLine
			.assertEqualTo("pattern" lineTo script("zero"))

		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.scriptingLine
			.assertEqualTo("pattern" lineTo script(
				"zero" lineTo script(),
				"plus" lineTo script("one")))

		pattern(
			choice(
				"zero" lineTo pattern(),
				"one" lineTo pattern()))
			.scriptingLine
			.assertEqualTo("pattern" lineTo script(
				"either" lineTo script("zero"),
				"either" lineTo script("one")))
	}
}