package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.pattern.choice
import leo13.pattern.lineTo
import leo13.pattern.pattern
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
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
		val compiler = patternCompiler(
			errorConverter(),
			false,
			patternDefinitions().plus(
				definition(
					"bit" lineTo pattern(),
					pattern(
						choice(
							"zero" lineTo pattern(),
							"one" lineTo pattern())))))

		compiler
			.process(token(opening("bit")))
			.process(token(closing))
			.assertEqualTo(
				compiler.set(
					pattern("bit" lineTo pattern(
						choice(
							"zero" lineTo pattern(),
							"one" lineTo pattern())))))
	}

	@Test
	fun processOptions() {
		patternCompiler()
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler().set(
					pattern(choice("zero", "one"))))
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
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(opening("two")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				patternCompiler.set(
					pattern(
						choice(
							"zero" lineTo pattern("resolved"),
							"one" lineTo pattern("resolved"),
							"two" lineTo pattern()))))
	}
}