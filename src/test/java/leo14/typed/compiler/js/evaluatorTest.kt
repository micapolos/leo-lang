package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.code.code
import leo14.lambda.js.expr.expr
import leo14.lambda.js.expr.term
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.typed.*
import leo14.typed.compiler.evaluator
import leo14.typed.compiler.parse
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun text() {
		emptyContext
			.evaluator()
			.parse(script(literal("foo")))
			.assertEqualTo(emptyContext.evaluator("'foo'".code.expr.term of textType))
	}

	@Test
	fun fieldsTest() {
		emptyContext
			.evaluator()
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script(literal(10)),
						"y" lineTo script(literal(20)))))
			.assertEqualTo(
				emptyContext
					.evaluator(
						"10".code.expr.term.plus("20".code.expr.term) of
							type("point" lineTo type("x" lineTo numberType, "y" lineTo numberType))))
	}
}