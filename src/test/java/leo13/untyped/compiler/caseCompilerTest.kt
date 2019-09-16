package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.caseTo
import leo13.untyped.expression.expression
import leo13.untyped.expression.lineTo
import leo13.untyped.pattern.eitherTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import kotlin.test.Test

class CaseCompilerTest {
	@Test
	fun process_success() {
		val caseCompiler = caseCompiler(
			errorConverter(),
			context(),
			"circle" eitherTo pattern("radius"))

		caseCompiler
			.process(token(opening("circle")))
			.process(token(opening("as")))
			.process(token(opening("square")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				caseCompiler
					.plus(
						compiled(
							"circle" caseTo expression("as" lineTo expression("square")),
							pattern(
								"radius" lineTo pattern(),
								"as" lineTo pattern("square")))))
	}
}
