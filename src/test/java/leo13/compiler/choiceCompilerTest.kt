package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.pattern.choice
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class ChoiceCompilerTest {
	@Test
	fun process() {
		ChoiceCompiler(
			errorConverter(),
			patternDefinitions(),
			choice())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.assertEqualTo(
				ChoiceCompiler(
					errorConverter(),
					patternDefinitions(),
					choice("zero", "one")))
	}
}