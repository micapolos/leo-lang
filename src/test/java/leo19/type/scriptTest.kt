package leo19.type

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.term.eval.value
import kotlin.test.Test

class ScriptTest {
	@Test
	fun emptyStructScript() {
		struct()
			.script(value())
			.assertEqualTo(script())
	}

	@Test
	fun simpleStructScript() {
		struct("number" fieldTo struct())
			.script(value())
			.assertEqualTo(script("number" lineTo script()))
	}

	@Test
	fun complexStructScript() {
		struct(
			"x" fieldTo choice("zero" caseTo struct(), "one" caseTo struct()),
			"y" fieldTo choice("zero" caseTo struct(), "one" caseTo struct()))
			.script(value(value(0), value(1)))
			.assertEqualTo(
				script(
					"x" lineTo script("zero"),
					"y" lineTo script("one")))
	}

	@Test
	fun simpleChoiceScript() {
		val type = choice(
			"false" caseTo struct(),
			"true" caseTo struct())
		type
			.script(value(0))
			.assertEqualTo(script("false"))
		type
			.script(value(1))
			.assertEqualTo(script("true"))
	}

	@Test
	fun complexChoiceScript() {
		val type =
			choice(
				"boolean" caseTo choice("false" caseTo struct(), "true" caseTo struct()),
				"bit" caseTo choice("zero" caseTo struct(), "one" caseTo struct()))
		type
			.script(value(value(0), value(0)))
			.assertEqualTo(script("boolean" lineTo script("false")))
		type
			.script(value(value(0), value(1)))
			.assertEqualTo(script("boolean" lineTo script("true")))
		type
			.script(value(value(1), value(0)))
			.assertEqualTo(script("bit" lineTo script("zero")))
		type
			.script(value(value(1), value(1)))
			.assertEqualTo(script("bit" lineTo script("one")))
	}
}