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
						"zero" eitherTo pattern("one"),
						"one" eitherTo pattern("two")))
					.plus("foo" lineTo pattern("bar"))
					.plus("zoo" lineTo pattern("zar")))
			.assertEqualTo(
				"pattern" lineTo script(
					"choice" lineTo script(
						"either" lineTo script(
							"zero" lineTo script("one")),
						"either" lineTo script(
							"one" lineTo script("two"))),
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")))
	}

	@Test
	fun reader_empty() {
		patternReader
			.unsafeValue("pattern" lineTo script())
			.assertEqualTo(pattern())
	}

	@Test
	fun reader_static() {
		patternReader
			.unsafeValue("pattern" lineTo script("zero"))
			.assertEqualTo(pattern("zero"))
	}

	@Test
	fun reader_dynamic() {
		patternReader
			.unsafeValue(
				"pattern" lineTo script(
					"choice" lineTo script(
						"either" lineTo script(
							"zero" lineTo script("one")),
						"either" lineTo script(
							"one" lineTo script("two"))),
					"foo" lineTo script("bar"),
					"zoo" lineTo script("zar")))
			.assertEqualTo(
				pattern(
					choice(
						"zero" eitherTo pattern("one"),
						"one" eitherTo pattern("two")))
					.plus("foo" lineTo pattern("bar"))
					.plus("zoo" lineTo pattern("zar")))
	}
}