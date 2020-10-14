package leo19.type

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.value.value
import kotlin.test.Test

class ScriptTest {
	@Test
	fun emptyStructScript() {
		type()
			.script(value())
			.assertEqualTo(script())
	}

	@Test
	fun simpleStructScript() {
		type("number" fieldTo type())
			.script(value())
			.assertEqualTo(script("number" lineTo script()))
	}

	@Test
	fun complexStructScript() {
		type(
			"x" fieldTo choice("zero" caseTo type(), "one" caseTo type()),
			"y" fieldTo choice("zero" caseTo type(), "one" caseTo type()))
			.script(value(value(0), value(1)))
			.assertEqualTo(
				script(
					"x" lineTo script("zero"),
					"y" lineTo script("one")))
	}

	@Test
	fun simpleChoiceScript() {
		val type = choice(
			"false" caseTo type(),
			"true" caseTo type())
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
				"boolean" caseTo choice("false" caseTo type(), "true" caseTo type()),
				"bit" caseTo choice("zero" caseTo type(), "one" caseTo type()))
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