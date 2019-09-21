package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.pattern.options
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class OptionsCompilerTest {
	@Test
	fun process() {
		OptionsCompiler(
			errorConverter(),
			patternDefinitions(),
			options())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.assertEqualTo(
				OptionsCompiler(
					errorConverter(),
					patternDefinitions(),
					options("zero", "one")))
	}
}