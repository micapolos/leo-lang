package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.expression.caseTo
import leo13.expression.expression
import leo13.expression.lineTo
import leo13.pattern.eitherTo
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
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
			.process(token(opening("to")))
			.process(token(opening("square")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				caseCompiler
					.plus(
						compiled(
							"circle" caseTo expression("to" lineTo expression("square")),
							pattern(
								"radius" lineTo pattern(),
								"to" lineTo pattern("square")))))
	}
}
