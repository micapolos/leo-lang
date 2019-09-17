package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.pattern.*
import kotlin.test.Test

class PatternCompilerTest {
	@Test
	fun name() {
		patternCompiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(patternCompiler().set(pattern("zero")))
	}

	@Test
	fun processChoice() {
		patternCompiler()
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
			.assertEqualTo(
				patternCompiler().set(
					pattern(item(choice(either("zero"), either("one"))))))
	}

	@Test
	fun resolution() {
		val bitPattern = pattern(
			"bit" lineTo pattern(
				choice(
					either("zero"),
					either("one"))))

		val compiler = patternCompiler(
			errorConverter(),
			patternArrows().plus(
				pattern("bit") arrowTo bitPattern))

		compiler
			.process(token(opening("bit")))
			.process(token(closing))
			.process(token(opening("and")))
			.process(token(opening("bit")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler
					.set(bitPattern.plus("and" lineTo bitPattern)))
	}
}