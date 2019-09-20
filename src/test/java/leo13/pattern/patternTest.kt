package leo13.pattern

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

	@Test
	fun contains() {
		pattern()
			.contains(pattern())
			.assertEqualTo(true)

		pattern("foo")
			.contains(pattern("foo"))
			.assertEqualTo(true)

		pattern("foo")
			.contains(pattern("bar"))
			.assertEqualTo(false)

		pattern(item(choice(either("zero"), either("one"))))
			.contains(pattern("zero"))
			.assertEqualTo(true)

		pattern(item(choice(either("zero"), either("one"))))
			.contains(pattern("one"))
			.assertEqualTo(true)

		pattern(item(choice(either("zero"), either("one"))))
			.contains(pattern("two"))
			.assertEqualTo(false)

		pattern(item(choice(either("zero"), either("one"))))
			.contains(pattern(item(choice(either("zero"), either("one")))))
			.assertEqualTo(true)

		pattern(item(choice(either("zero"), either("one"))))
			.contains(pattern(item(choice(either("one"), either("zero")))))
			.assertEqualTo(false)

		pattern(item(pattern("zero") arrowTo pattern("one")))
			.contains(pattern(item(pattern("zero") arrowTo pattern("one"))))
			.assertEqualTo(true)

		pattern(item(pattern("zero") arrowTo pattern("one")))
			.contains(pattern(item(pattern("one") arrowTo pattern("zero"))))
			.assertEqualTo(false)
	}

	@Test
	fun setOrNull() {
		val pattern = pattern(
			"point" lineTo pattern(
				"x" lineTo pattern("zero"),
				"y" lineTo pattern("one")))

		pattern
			.setOrNull("x" lineTo pattern("two"))
			.assertEqualTo(
				pattern(
					"point" lineTo pattern(
						"x" lineTo pattern("two"),
						"y" lineTo pattern("one"))))

		pattern
			.setOrNull("y" lineTo pattern("two"))
			.assertEqualTo(
				pattern(
					"point" lineTo pattern(
						"x" lineTo pattern("zero"),
						"y" lineTo pattern("two"))))

		pattern()
			.setOrNull("x" lineTo pattern("two"))
			.assertEqualTo(null)

		pattern(choice(either("point"), either("line")))
			.setOrNull("x" lineTo pattern("two"))
			.assertEqualTo(null)
	}
}