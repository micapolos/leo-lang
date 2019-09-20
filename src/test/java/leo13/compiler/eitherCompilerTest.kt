package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.pattern.eitherTo
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class EitherCompilerTest {
	@Test
	fun resolution() {
		val eitherCompiler =
			EitherCompiler(
				errorConverter(),
				patternDefinitions().plus(
					definition(
						"zero" lineTo pattern(),
						pattern("resolved"))),
				null)

		eitherCompiler
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(eitherCompiler.copy(eitherOrNull = "zero" eitherTo pattern("resolved")))
	}
}