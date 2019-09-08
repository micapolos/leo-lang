package leo13.sentence

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class SentenceTest {
	@Test
	fun string() {
		sentence(
			"vec" lineTo sentence(
				"x" lineTo sentence("zero"),
				"y" lineTo sentence("one")),
			"plus" lineTo sentence(
				"vec" lineTo sentence(
					"x" lineTo sentence("two"),
					"y" lineTo sentence("three"))))
			.toString()
			.assertEqualTo(
				linesString(
					"vec",
					"\tx: zero",
					"\ty: one",
					"plus: vec",
					"\tx: two",
					"\ty: three"))
	}
}