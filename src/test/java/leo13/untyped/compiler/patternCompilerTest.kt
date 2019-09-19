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
	fun resolution() {
		val bitPattern = pattern(
			"bit" lineTo pattern(
				choice(
					either("zero"),
					either("one"))))

		val compiler = patternCompiler(
			errorConverter(),
			false,
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

	@Test
	fun processEithers() {
		patternCompiler()
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler().set(
					pattern(item(choice(either("zero"), either("one"))))))
	}

	@Test
	fun processEithersAndOthers() {
		patternCompiler()
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("plus")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler().set(
					pattern(choice(either("zero"))).plus("plus" lineTo pattern("one"))))
	}

	@Test
	fun processOthersAndOrs() {
		patternCompiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("or")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("or")))
			.process(token(opening("two")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler().set(
					pattern(choice(either("zero"), either("one"), either("two")))))
	}

	@Test
	fun processResolution() {
		val patternCompiler = patternCompiler(
			errorConverter(),
			false,
			patternArrows()
				.plus(pattern("zero") arrowTo pattern("zero" lineTo pattern("resolved")))
				.plus(pattern("one") arrowTo pattern("one" lineTo pattern("resolved"))))

		patternCompiler
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("or")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("or")))
			.process(token(opening("two")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler.set(
					pattern(
						choice(
							"zero" eitherTo pattern("resolved"),
							"one" eitherTo pattern("resolved"),
							either("two")))))
	}
}