package leo13.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.assertFails

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
			options(
				"zero" lineTo pattern(),
				"one" lineTo pattern()))
			.scriptingLine
			.assertEqualTo("pattern" lineTo script(
				"options" lineTo script("zero", "one")))
	}

	@Test
	fun recurseExpand() {
		assertFails { pattern(recurse).recurseExpand }

		pattern()
			.recurseExpand
			.assertEqualTo(pattern())

		pattern("foo")
			.recurseExpand
			.assertEqualTo(pattern("foo"))

		pattern(
			"x" lineTo pattern("zero"),
			"y" lineTo pattern("one"))
			.recurseExpand
			.assertEqualTo(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one")))

		pattern("zero" lineTo pattern(recurse))
			.recurseExpand
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(recurse))))

		pattern("zero" lineTo pattern(recurse))
			.recurseExpand
			.recurseExpand
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(recurse))))

		pattern("zero" lineTo pattern("one" lineTo pattern(recurse)))
			.recurseExpand
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(recurse))))

		pattern("zero" lineTo pattern("one" lineTo pattern(recurse.recurse)))
			.recurseExpand
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(recurse.recurse))))))

		pattern("zero" lineTo pattern("one" lineTo pattern(recurse.recurse)))
			.recurseExpand
			.recurseExpand
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(recurse.recurse))))))
	}

	@Test
	fun contains() {
		pattern().contains(pattern()).assertEqualTo(true)

		pattern("zero").contains(pattern("zero")).assertEqualTo(true)
		pattern("zero").contains(pattern("one")).assertEqualTo(false)

		pattern(
			"x" lineTo pattern("zero"),
			"y" lineTo pattern("one"))
			.contains(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one")))
			.assertEqualTo(true)

		pattern(options("zero", "one")).contains(pattern("zero")).assertEqualTo(true)
		pattern(options("zero", "one")).contains(pattern("one")).assertEqualTo(true)
		pattern(options("zero", "one")).contains(pattern("two")).assertEqualTo(false)

		pattern("bit" lineTo pattern(options("zero", "one")))
			.contains(pattern("bit" lineTo pattern("zero")))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(recurse))
			.contains(pattern("zero" lineTo pattern(recurse)))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(recurse))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(recurse))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(recurse))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(
							"zero" lineTo pattern(recurse)))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern("one" lineTo pattern(recurse.recurse)))
			.contains(pattern("zero" lineTo pattern("one" lineTo pattern(recurse.recurse))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern("one" lineTo pattern(recurse.recurse)))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(
									recurse.recurse))))))
			.assertEqualTo(true)
	}
}