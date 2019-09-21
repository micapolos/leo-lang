package leo13.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun scriptingLine() {
		choice()
			.scriptingLine
			.assertEqualTo("choice" lineTo script())

		choice(
			"zero" lineTo pattern("foo"),
			"one" lineTo pattern("bar"))
			.scriptingLine
			.assertEqualTo(
				"choice" lineTo script(
					"either" lineTo script("zero" lineTo script("foo")),
					"either" lineTo script("one" lineTo script("bar"))))
	}
}