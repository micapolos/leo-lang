package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.expression
import leo13.untyped.expression.given
import leo13.untyped.pattern.arrowTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import leo13.untyped.pattern.plus
import leo13.untyped.value.function
import leo13.untyped.value.value
import kotlin.test.Test

class DefineCompilerTest {
	@Test
	fun processHas() {
		DefineCompiler(
			errorConverter(),
			context(),
			pattern())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("has")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context().plus(pattern("zero") arrowTo pattern("zero" lineTo pattern("one"))),
					pattern()))
	}

	@Test
	fun processGives() {
		DefineCompiler(
			errorConverter(),
			context(),
			pattern())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("gives")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context().plus(
						compiled(
							function(given(value()), expression("one")),
							pattern("zero") arrowTo pattern("one"))),
					pattern()))
	}

	@Test
	fun processGivesComplex() {
		DefineCompiler(
			errorConverter(),
			context(),
			pattern())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("plus")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("gives")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context().plus(
						compiled(
							function(given(value()), expression("one")),
							pattern("zero").plus("plus" lineTo pattern("one")) arrowTo pattern("one"))),
					pattern()))
	}
}