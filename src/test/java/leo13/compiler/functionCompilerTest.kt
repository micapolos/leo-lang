package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.expression.expression
import leo13.expression.valueContext
import leo13.pattern.arrowTo
import leo13.pattern.choice
import leo13.pattern.lineTo
import leo13.pattern.pattern
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
			pattern(),
			null)
			.process(token(opening("bit")))
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
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
					pattern(),
					compiled(
						function(
							valueContext(),
							expression("foo")),
						pattern("bit" lineTo pattern(
							choice(
								"zero" lineTo pattern(),
								"one" lineTo pattern()))) arrowTo
							pattern("foo"))))
	}
}