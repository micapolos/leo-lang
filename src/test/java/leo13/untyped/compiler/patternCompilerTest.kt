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
			patternDefinitions().plus(
				definition(
					"bit" lineTo pattern(),
					pattern(
						choice(
							either("zero"),
							either("one"))))))

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
	fun processResolution() {
		val patternCompiler = patternCompiler(
			errorConverter(),
			false,
			patternDefinitions()
				.plus(definition("zero" lineTo pattern(), pattern("resolved")))
				.plus(definition("one" lineTo pattern(), pattern("resolved"))))

		patternCompiler
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
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