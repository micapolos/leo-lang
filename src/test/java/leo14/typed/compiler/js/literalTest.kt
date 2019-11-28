package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.js.ast.expr
import leo14.lambda.term
import leo14.literal
import leo14.typed.numberLine
import leo14.typed.of
import leo14.typed.textLine
import kotlin.test.Test

class LiteralTest {
	@Test
	fun literalTypedLine() {
		literal("foo")
			.typedLine
			.assertEqualTo(term(expr("foo")) of textLine)

		literal(10)
			.typedLine
			.assertEqualTo(term(expr(10)) of numberLine)

		literal(10.2)
			.typedLine
			.assertEqualTo(term(expr(10.2)) of numberLine)
	}
}