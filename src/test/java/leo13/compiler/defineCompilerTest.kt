package leo13.compiler

import leo.base.assertEqualTo
import leo13.containsName
import leo13.errorConverter
import leo13.expression.expression
import leo13.expression.valueContext
import leo13.type.*
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
			type())
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
						.plus(definition("bit" lineTo type(), type("one")))
						.plus("bit" lineTo type("one")),
					type()))
	}

	@Test
	fun processGives() {
		DefineCompiler(
			errorConverter(),
			context(),
			type())
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
						typed(
							function(valueContext(), expression("one")),
							type("zero") arrowTo type("one"))),
					type()))
	}

	@Test
	fun processGivesComplex() {
		DefineCompiler(
			errorConverter(),
			context(),
			type())
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
						typed(
							function(valueContext(), expression("one")),
							type("zero").plus("plus" lineTo type("one")) arrowTo type("one"))),
					type()))
	}

	@Test
	fun processContainsRecurse() {
		val defineCompiler = DefineCompiler(
			errorConverter(),
			context(),
			type())

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
								"list" lineTo type("link"),
								type(onceRecurse.increase)))
						.plus("list" lineTo type("link" lineTo type(onceRecurse.increase))),
					type()))
	}
}