package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.givesName
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.type.*
import kotlin.test.Test

class TypeCompilerTest {
	@Test
	fun name() {
		typeCompiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(typeCompiler().set(type("zero")))
	}

	@Test
	fun resolution() {
		val compiler = TypeCompiler(
			errorConverter(),
			false,
			typeContext().plus(
				definition(
					"bit" lineTo type(),
					type(
						options(
							"zero" lineTo type(),
							"one" lineTo type())))),
			type())

		compiler
			.process(token(opening("bit")))
			.process(token(closing))
			.assertEqualTo(
				compiler.set(
					type("bit" lineTo type(
						options(
							"zero" lineTo type(),
							"one" lineTo type())))))
	}

	@Test
	fun processOptions() {
		typeCompiler()
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				typeCompiler().set(
					type(options("zero", "one"))))
	}

	@Test
	fun processResolution() {
		val typeCompiler = TypeCompiler(
			errorConverter(),
			false,
			typeContext()
				.plus(definition("zero" lineTo type(), type("resolved")))
				.plus(definition("one" lineTo type(), type("resolved"))),
			type())

		typeCompiler
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(opening("two")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				typeCompiler.set(
					type(
						options(
							"zero" lineTo type("resolved"),
							"one" lineTo type("resolved"),
							"two" lineTo type()))))
	}

	@Test
	fun processRecurse() {
		val typeCompiler = TypeCompiler(
			errorConverter(),
			false,
			typeContext().trace("list").trace("link"),
			type())

		typeCompiler
			.process(token(opening("link")))
			.process(token(closing))
			.assertEqualTo(typeCompiler.set(type(onceRecurse)))

		typeCompiler
			.process(token(opening("list")))
			.process(token(closing))
			.assertEqualTo(typeCompiler.set(type(onceRecurse.increase)))

		typeCompiler
			.process(token(opening("other")))
			.process(token(closing))
			.assertEqualTo(typeCompiler.set(type("other")))
	}

	@Test
	fun processGives() {
		typeCompiler()
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(opening(givesName)))
			.process(token(opening("bar")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(typeCompiler().set(type(type("foo") arrowTo type("bar"))))
	}
}