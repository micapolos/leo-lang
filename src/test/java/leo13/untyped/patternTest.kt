package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import kotlin.test.Test

class PatternTest {
	@Test
	fun writer() {
		patternWriter
			.scriptLine(pattern())
			.assertEqualTo("pattern" lineTo script())

		patternWriter
			.scriptLine(pattern("foo"))
			.assertEqualTo("pattern" lineTo script("foo"))

		patternWriter
			.scriptLine(
				pattern(
					"foo" lineTo pattern("bar"),
					"zoo" lineTo pattern("zar")))
			.assertEqualTo(
				"pattern" lineTo script(
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")))

		patternWriter
			.scriptLine(
				pattern(
					choice(
						"zero" optionTo pattern("one"),
						"one" optionTo pattern("two")),
					"foo" lineTo pattern("bar"),
					"zoo" lineTo pattern("zar")))
			.assertEqualTo(
				"pattern" lineTo script(
					"choice" lineTo script(
						"option" lineTo script(
							"zero" lineTo script("one")),
						"option" lineTo script(
							"one" lineTo script("two"))),
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")))
	}

	@Test
	fun reader() {
		patternReader
			.unsafeValue("pattern" lineTo script())
			.assertEqualTo(pattern())

		patternReader
			.unsafeValue(
				"pattern" lineTo script(
					"choice" lineTo script(
						"option" lineTo script(
							"zero" lineTo script("one")),
						"option" lineTo script(
							"one" lineTo script("two"))),
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")))
			.assertEqualTo(
				pattern(
					choice(
						"zero" optionTo pattern("one"),
						"one" optionTo pattern("two")),
					"foo" lineTo pattern("bar"),
					"zoo" lineTo pattern("zar")))
	}
}