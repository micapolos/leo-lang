package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import org.junit.Test

class ChoiceTest {
	@Test
	fun writer() {
		choiceWriter
			.scriptLine(
				choice(
					"zero" optionTo pattern("foo"),
					"one" optionTo pattern("bar")))
			.assertEqualTo(
				"choice" lineTo script(
					"option" lineTo script(
						"zero" lineTo script("foo")),
					"option" lineTo script(
						"one" lineTo script("bar"))))
	}

	@Test
	fun reader() {
		choiceReader
			.unsafeValue(
				"choice" lineTo script(
					"option" lineTo script(
						"zero" lineTo script("foo")),
					"option" lineTo script(
						"one" lineTo script("bar"))))
			.assertEqualTo(
				choice(
					"zero" optionTo pattern("foo"),
					"one" optionTo pattern("bar")))
	}
}