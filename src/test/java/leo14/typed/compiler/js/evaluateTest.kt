package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.typed.compiler.evaluate
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun text() {
		emptyContext
			.evaluate(script(literal("123")))
			.assertEqualTo(script("javascript" lineTo script(literal("'123'"))))
	}

	@Test
	fun struct() {
		emptyContext
			.evaluate(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20)))))
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
		emptyContext
			.evaluate(
				script(
					line(literal(10)),
					"plus" lineTo script(literal(20))))
			.assertEqualTo(
				script(
					"javascript" lineTo script(
						literal("(10)+(20)"))))
	}
}
