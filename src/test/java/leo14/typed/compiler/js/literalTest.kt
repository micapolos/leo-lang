package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.js.ast.expr
import leo14.literal
import leo14.typed.lineTo
import leo14.typed.nativeTyped
import leo14.typed.numberName
import leo14.typed.textName
import kotlin.test.Test

class LiteralTest {
	@Test
	fun literalTypedLine() {
		literal("foo")
			.typedLine
			.assertEqualTo(textName lineTo nativeTyped(expr("foo")))

		literal(10)
			.typedLine
			.assertEqualTo(numberName lineTo nativeTyped(expr(10)))

		literal(10.2)
			.typedLine
			.assertEqualTo(numberName lineTo nativeTyped(expr(10.2)))
	}
}