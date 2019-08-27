package leo13.type.pattern

import leo.base.assertEqualTo
import leo13.script.assertEqualsToScriptLine
import kotlin.test.Test

class PatternTest {
	@Test
	fun scriptable() {
		pattern()
			.assertEqualsToScriptLine("pattern()")

		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.assertEqualsToScriptLine("pattern(zero()plus(one()))")

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.assertEqualsToScriptLine("pattern(choice(zero()one()))")

		pattern(choice())
			.assertEqualsToScriptLine("pattern(choice())")

		pattern("choice" lineTo pattern())
			.assertEqualsToScriptLine("pattern(meta(choice()))")

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.plus("two" lineTo pattern())
			.plus("three" lineTo pattern())
			.assertEqualsToScriptLine("pattern(choice(zero()one())two()three())")

		pattern(
			"choice" lineTo pattern(
				"zero" lineTo pattern(),
				"one" lineTo pattern()),
			"two" lineTo pattern(),
			"three" lineTo pattern())
			.assertEqualsToScriptLine("pattern(meta(choice(zero()one()))two()three())")

		pattern(pattern("zero") arrowTo pattern("one"))
			.assertEqualsToScriptLine("pattern(arrow(zero()to(one())))")

		pattern("arrow" lineTo pattern())
			.assertEqualsToScriptLine("pattern(meta(arrow()))")

	}

	@Test
	fun contains() {
		pattern()
			.contains(pattern())
			.assertEqualTo(true)

		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.contains(
				pattern(
					"zero" lineTo pattern(),
					"plus" lineTo pattern("one")))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern("zero" lineTo pattern()))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(pattern(
				choice(
					"zero" caseTo pattern(),
					"one" caseTo pattern())))
			.assertEqualTo(true)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(
					choice(
						"one" caseTo pattern(),
						"zero" caseTo pattern())))
			.assertEqualTo(false)

		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.contains(
				pattern(choice("zero" caseTo pattern())))
			.assertEqualTo(false)
	}
}