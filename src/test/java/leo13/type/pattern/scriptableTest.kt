package leo13.type.pattern

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class ScriptableTest {
	@Test
	fun empty() {
		type()
			.scriptableBody
			.assertEqualTo(script())
	}

	@Test
	fun lines() {
		type(
			"zero" lineTo type(),
			"plus" lineTo type("one"))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"plus" lineTo script(
						"one" lineTo script())))
	}

	@Test
	fun choice() {
		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type(),
				"two" caseTo type()))
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
		type(
			"zero" lineTo type(),
			"or" lineTo type("one" lineTo type()),
			"or" lineTo type("two" lineTo type()))
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
		type(
			choice(
				"zero" caseTo type(),
				"one" caseTo type()))
			.plus("two" lineTo type())
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
		type(
			arrow(
				type("zero"),
				type("one")))
			.scriptableBody
			.assertEqualTo(
				script(
					"zero" lineTo script(),
					"to" lineTo script(
						"one" lineTo script())))
	}
}