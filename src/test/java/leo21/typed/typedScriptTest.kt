package leo21.typed

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.stringLine
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun empty() {
		typed().script.assertEqualTo(script())
	}

	@Test
	fun number() {
		typed(10.0).script.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun text() {
		typed("foo").script.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun simpleStruct() {
		typed("x" lineTo typed(10.0))
			.script
			.assertEqualTo(script("x" lineTo script(literal(10.0))))
	}

	@Test
	fun struct() {
		typed(
			"x" lineTo typed(10.0),
			"y" lineTo typed(20.0))
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10.0)),
					"y" lineTo script(literal(20.0))))
	}

	@Test
	fun choice_first() {
		choice(stringLine, doubleLine)
			.typed(line("foo"))
			.script
			.assertEqualTo(script(literal("foo")))
	}


	@Test
	fun choice_second() {
		choice(stringLine, doubleLine)
			.typed(line(10.0))
			.script
			.assertEqualTo(script(literal(10.0)))
	}
}