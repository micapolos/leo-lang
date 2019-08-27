package leo13.type.pattern

import leo.base.assertEqualTo
import leo13.script.assertEqualsToScriptLine
import kotlin.test.Test

class PatternTest {
	@Test
	fun scriptableEmpty() {
		pattern()
			.assertEqualsToScriptLine("pattern()")
	}

	@Test
	fun scriptableLines() {
		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.assertEqualsToScriptLine("pattern(zero()plus(one()))")
	}

	@Test
	fun scriptableEmptyChoice() {
		pattern(choice())
			.assertEqualsToScriptLine("pattern(choice())")
	}

	@Test
	fun scriptableChoice() {
		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.assertEqualsToScriptLine("pattern(choice(zero()one()))")
	}

	@Test
	fun scriptableChoiceLine() {
		pattern("choice" lineTo pattern())
			.assertEqualsToScriptLine("pattern(meta(choice()))")
	}

	@Test
	fun scriptableChoiceAndLines() {
		pattern(
			choice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.plus("two" lineTo pattern())
			.plus("three" lineTo pattern())
			.assertEqualsToScriptLine("pattern(choice(zero()one())two()three())")
	}

	@Test
	fun scriptableArrow() {
		pattern(pattern("zero") arrowTo pattern("one"))
			.assertEqualsToScriptLine("pattern(arrow(zero()to(one())))")
	}

	@Test
	fun scriptableArrowLine() {
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