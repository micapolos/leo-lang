package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.Processor
import leo13.errorConverter
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.expression
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.pattern
import org.junit.Test

class ExpressionCompilerTest {
	@Test
	fun compile() {
		errorConverter<ExpressionCompiled, Processor<Token>>()
			.expressionCompiler()
			.process(token(opening("foo")))
			.process(token(closing))
			.assertEqualTo(
				errorConverter<ExpressionCompiled, Processor<Token>>()
					.expressionCompiler(
						context(),
						compiled(
							expression(plus("foo").op),
							pattern("foo"))))
	}
}
