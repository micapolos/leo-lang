package leo13.untyped.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ChoiceTest {
	@Test
	fun writer() {
		choice(
			"zero" eitherTo pattern("foo"),
			"one" eitherTo pattern("bar"))
			.scriptLine
			.assertEqualTo(
				"choice" lineTo script(
					"either" lineTo script(
						"zero" lineTo script("foo")),
					"either" lineTo script(
						"one" lineTo script("bar"))))
	}

	@Test
	fun reader() {
		("choice" lineTo script())
			.unsafeChoice
			.assertEqualTo(choice())

		("zero" lineTo script("foo"))
			.unsafeChoice
			.assertEqualTo(choice("zero" eitherTo pattern("foo")))

		("choice" lineTo script(
			"either" lineTo script(
						"zero" lineTo script("foo")),
			"either" lineTo script(
						"one" lineTo script("bar"))))
			.unsafeChoice
			.assertEqualTo(
				choice(
					"zero" eitherTo pattern("foo"),
					"one" eitherTo pattern("bar")))

	}
}