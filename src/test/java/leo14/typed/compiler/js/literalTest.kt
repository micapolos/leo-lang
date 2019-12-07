package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.lambda.js.expr.expr
import leo14.lambda.js.expr.term
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
			.assertEqualTo(literal("foo").expr.term of textLine)

		literal(10)
			.typedLine
			.assertEqualTo(literal(10).expr.term of numberLine)

		literal(10.2)
			.typedLine
			.assertEqualTo(literal(10.2).expr.term of numberLine)
	}
}