package leo13.type

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ScriptableTest {
	@Test
	fun empty() {
		pattern()
			.scriptableBody
			.assertEqualTo(script())
	}

	@Test
	fun lines() {
		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script(
						"one" lineTo script())))
	}

	@Test
	fun choice() {
		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern(),
				"two" caseTo pattern()))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"or" lineTo script(
						"one" lineTo script()),
					"or" lineTo script(
						"two" lineTo script())))
	}

	@Test
	fun choiceLines() {
		pattern(
			"zero" lineTo pattern(),
			"or" lineTo pattern("one" lineTo pattern()),
			"or" lineTo pattern("two" lineTo pattern()))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"meta" lineTo script(
						"or" lineTo script(
							"one" lineTo script())),
					"or" lineTo script(
						"two" lineTo script())))
	}

	@Test
	fun choiceAndLines() {
		pattern(
			unsafeChoice(
				"zero" caseTo pattern(),
				"one" caseTo pattern()))
			.plus("two" lineTo pattern())
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"or" lineTo script(
						"one" lineTo script()),
					"two" lineTo script()))
	}

	@Test
	fun scriptableArrow() {
		pattern(
			arrow(
				type(pattern("zero")),
				type(pattern("one"))))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"to" lineTo script(
						"one" lineTo script())))
	}
}