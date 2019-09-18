package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.pattern.arrowTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import kotlin.test.Test

class DefineCompilerTest {
	@Test
	fun processHas() {
		DefineCompiler(
			errorConverter(),
			context(),
			pattern())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("has")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context().plus(pattern("zero") arrowTo pattern("zero" lineTo pattern("one"))),
					pattern()))
	}
}