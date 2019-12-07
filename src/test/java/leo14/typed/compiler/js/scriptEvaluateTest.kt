package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptEvaluateTest {
	@Test
	fun text() {
		script(literal("123"))
			.evaluate
			.assertEqualTo(script("javascript" lineTo script(literal("'123'"))))
	}

	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10)),
				"y" lineTo script(literal(20))))
			.evaluate
			.assertEqualTo(
				script(
					"point" lineTo script(
						"x" lineTo script(
							"javascript" lineTo script(literal("10"))),
						"y" lineTo script(
							"javascript" lineTo script(literal("20"))))))
	}

	@Test
	fun numberPlusNumber() {
		script(
			line(literal(10)),
			"plus" lineTo script(literal(20)))
			.evaluate
			.assertEqualTo(
				script(
					"javascript" lineTo script(
						literal("(10)+(20)"))))
	}

	@Test
	fun give() {
		script(
			2.literal.line,
			"give" lineTo script(
				"given".line,
				"number".line,
				"plus" lineTo script(
					"given".line,
					"number".line)))
			.evaluate
			.assertEqualTo(
				script(
					"javascript" lineTo script(literal("(2)+(2)"))))
	}
}
