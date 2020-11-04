package leo21.evaluated

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.line
import leo21.type.lineTo
import leo21.type.stringType
import leo21.typed.LineTyped
import leo21.typed.evaluated
import leo21.typed.line
import leo21.typed.lineTo
import leo21.typed.typed
import kotlin.test.Test

class EvaluatedScriptTest {
	@Test
	fun number() {
		typed(10.0)
			.evaluated
			.script
			.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun string() {
		typed("foo")
			.evaluated
			.script
			.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun struct() {
		typed(
			"x" lineTo typed(10.0),
			"y" lineTo typed(),
			"z" lineTo typed(20.0))
			.evaluated
			.script
			.assertEqualTo(
				script(
					"x" lineTo script(literal(10.0)),
					"y" lineTo script(),
					"z" lineTo script(literal(20.0))))
	}

	@Test
	fun choice_rhs() {
		choice(
			"x" lineTo stringType,
			"y" lineTo stringType)
			.typed("y" lineTo typed("foo"))
			.evaluated
			.script
			.assertEqualTo(script("y" lineTo script(literal("foo"))))
	}

	@Test
	fun choice_lhs() {
		choice(
			"x" lineTo stringType,
			"y" lineTo stringType)
			.typed("x" lineTo typed("foo"))
			.evaluated
			.script
			.assertEqualTo(script("x" lineTo script(literal("foo"))))
	}

	@Test
	fun function() {
		typed(LineTyped(id(), line(stringType arrowTo doubleType)))
			.evaluated
			.script
			.assertEqualTo(
				script(
					"function" lineTo script(
						"text" lineTo script(),
						"doing" lineTo script("number"))))
	}
}