package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.expression
import leo13.untyped.expression.given
import leo13.untyped.pattern.*
import leo13.untyped.value.function
import leo13.untyped.value.value
import kotlin.test.Test

class FunctionCompilerTest {
	@Test
	fun process() {
		functionCompiler()
			.process(token(opening("bit")))
			.process(token(opening("choice")))
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
			.process(token(opening("one")))
			.process(token(closing))
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
					pattern("bit" lineTo pattern(item(choice(either("zero"), either("one"))))),
					compiled(
						function(
							given(value()),
							expression("foo")),
						pattern("bit" lineTo pattern(item(choice(either("zero"), either("one"))))) arrowTo pattern("foo"))))
	}
}