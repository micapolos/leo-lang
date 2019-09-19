package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.pattern.arrowTo
import leo13.untyped.pattern.eitherTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import kotlin.test.Test

class EitherCompilerTest {
	@Test
	fun resolution() {
		val eitherCompiler =
			EitherCompiler(
				errorConverter(),
				patternArrows().plus(pattern("zero") arrowTo pattern("zero" lineTo pattern("resolved"))),
				null)

		eitherCompiler
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(eitherCompiler.copy(eitherOrNull = "zero" eitherTo pattern("resolved")))
	}
}