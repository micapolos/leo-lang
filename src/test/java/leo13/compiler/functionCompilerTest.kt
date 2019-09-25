package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.expression.expression
import leo13.expression.valueContext
import leo13.type.arrowTo
import leo13.type.lineTo
import leo13.type.options
import leo13.type.type
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.value.function
import kotlin.test.Test

class FunctionCompilerTest {
	@Test
	fun process() {
		FunctionCompiler(
			errorConverter(),
			context(),
			type(),
			null)
			.process(token(opening("bit")))
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("gives")))
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				FunctionCompiler(
					errorConverter(),
					context(),
					type(),
					compiled(
						function(
							valueContext(),
							expression("foo")),
						type("bit" lineTo type(options("zero", "one"))) arrowTo type("foo"))))
	}
}