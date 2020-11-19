package leo21.evaluator

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.lambda.value.value
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.compiled.LineCompiled
import leo21.compiled.compiled
import leo21.compiled.lineTo
import leo21.prim.prim
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.numberType
import leo21.type.line
import leo21.type.lineTo
import leo21.type.stringType
import kotlin.test.Test

class EvaluatedScriptTest {
	@Test
	fun number() {
		compiled(10.0)
			.evaluated
			.script
			.assertEqualTo(script(literal(10.0)))
	}

	@Test
	fun string() {
		compiled("foo")
			.evaluated
			.script
			.assertEqualTo(script(literal("foo")))
	}

	@Test
	fun struct() {
		compiled(
			"x" lineTo compiled(10.0),
			"y" lineTo compiled(),
			"z" lineTo compiled(20.0))
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
			.compiled("y" lineTo compiled("foo"))
			.evaluated
			.script
			.assertEqualTo(script("y" lineTo script(literal("foo"))))
	}

	@Test
	fun choice_lhs() {
		choice(
			"x" lineTo stringType,
			"y" lineTo stringType)
			.compiled("x" lineTo compiled("foo"))
			.evaluated
			.script
			.assertEqualTo(script("x" lineTo script(literal("foo"))))
	}

	@Test
	fun function() {
		compiled(LineCompiled(id(), line(stringType arrowTo numberType)))
			.evaluated
			.script
			.assertEqualTo(
				script(
					"function" lineTo script(
						"text" lineTo script(),
						"does" lineTo script("number"))))
	}

	@Test
	fun double() {
		Evaluated(value(prim(10.0)), numberType)
			.script
			.assertEqualTo(script(literal(10)))
	}
}