package leo21.typed

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun empty() {
		typed()
			.script
			.assertEqualTo(script())
	}

	@Test
	fun string() {
		typed("zero")
			.script
			.assertEqualTo(script(literal("zero")))
	}

	@Test
	fun double() {
		typed(10)
			.script
			.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun lines() {
		typed(
			typedLine(10),
			typedLine("ten"),
			"x" lineTo typed("zero" lineTo typed()))
			.script
			.assertEqualTo(
				script(
					line(literal(10.0)),
					line(literal("ten")),
					"x" lineTo script("zero")))
	}
}