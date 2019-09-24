package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.pattern.*
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
		val compiler = PatternCompiler(
			errorConverter(),
			false,
			patternDefinitions().plus(
				definition(
					"bit" lineTo pattern(),
					pattern(
						options(
							"zero" lineTo pattern(),
							"one" lineTo pattern())))),
			null,
			null,
			pattern())

		compiler
			.process(token(opening("bit")))
			.process(token(closing))
			.assertEqualTo(
				compiler.set(
					pattern("bit" lineTo pattern(
						options(
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
					pattern(options("zero", "one"))))
	}

	@Test
	fun processResolution() {
		val patternCompiler = PatternCompiler(
			errorConverter(),
			false,
			patternDefinitions()
				.plus(definition("zero" lineTo pattern(), pattern("resolved")))
				.plus(definition("one" lineTo pattern(), pattern("resolved"))),
			null,
			null,
			pattern())

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
						options(
							"zero" lineTo pattern("resolved"),
							"one" lineTo pattern("resolved"),
							"two" lineTo pattern()))))
	}

	@Test
	fun processRecurseResolution() {
		val patternCompiler = PatternCompiler(
			errorConverter(),
			false,
			patternDefinitions(),
			definition("foo" lineTo pattern("bar"), onceRecurse.increase),
			null,
			pattern())

		patternCompiler
			.process(token(opening("zoo")))
			.process(token(opening("foo")))
			.process(token(opening("bar")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(patternCompiler
				.set(pattern("zoo" lineTo pattern(onceRecurse.increase.increase))))
	}
}