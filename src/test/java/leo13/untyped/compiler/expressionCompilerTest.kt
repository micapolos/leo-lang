package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.expression
import leo13.untyped.expression.lineTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import org.junit.Test

class ExpressionCompilerTest {
	@Test
	fun compile() {
		expressionCompiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("plus")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression(
							"zero" lineTo expression(),
							"plus" lineTo expression("one")),
						pattern(
							"zero" lineTo pattern(),
							"plus" lineTo pattern("one")))))
	}
}
