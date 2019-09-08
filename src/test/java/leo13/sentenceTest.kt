package leo13

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class SentenceTest {
	@Test
	fun string() {
		sentence(
			"vec" plus sentence(
				"x" plus sentence("zero"),
				"y" plus sentence("one")),
			"plus" plus sentence(
				"vec" plus sentence(
					"x" plus sentence("two"),
					"y" plus sentence("three"))))
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