package leo13.compiler

import leo.base.assertEqualTo
import leo13.containsName
import leo13.errorConverter
import leo13.expression.expression
import leo13.expression.valueContext
import leo13.pattern.*
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.value.function
import kotlin.test.Test

class DefineCompilerTest {
	@Test
	fun processContains() {
		DefineCompiler(
			errorConverter(),
			context(),
			pattern())
			.process(token(opening("bit")))
			.process(token(closing))
			.process(token(opening(containsName)))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context()
						.plus(definition("bit" lineTo pattern(), pattern("one")))
						.plus("bit" lineTo pattern("one")),
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
							function(valueContext(), expression("one")),
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
							function(valueContext(), expression("one")),
							pattern("zero").plus("plus" lineTo pattern("one")) arrowTo pattern("one"))),
					pattern()))
	}

	@Test
	fun processContainsRecurse() {
		val defineCompiler = DefineCompiler(
			errorConverter(),
			context(),
			pattern())

		defineCompiler
			.process(token(opening("list")))
			.process(token(opening("link")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("contains")))
			.process(token(opening("list")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				DefineCompiler(
					errorConverter(),
					context()
						.plus(
							definition(
								"list" lineTo pattern("link"),
								pattern(onceRecurse.increase)))
						.plus("list" lineTo pattern("link" lineTo pattern(onceRecurse.increase))),
					pattern()))
	}
}