package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.expression.*
import leo13.matchingName
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.type.lineTo
import leo13.type.options
import leo13.type.type
import kotlin.test.Test

class SwitchCompilerTest {
	@Test
	fun process() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			options(
				"square" lineTo type("side"),
				"circle" lineTo type("radius")),
			typed(switch(), type("lhs")))

		switchCompiler
			.process(token(opening("circle")))
			.process(token(opening("circled")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				switchCompiler(
					errorConverter(),
					context(),
					options("square" lineTo type("side")),
					typed(
						switch("circle" caseTo expression("circled")),
						type("circled"))))
	}

	@Test
	fun processMatching() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			options(
				"square" lineTo type("side"),
				"circle" lineTo type("radius")),
			typed(switch(), type("lhs")))

		switchCompiler
			.process(token(opening("circle")))
			.process(token(opening(matchingName)))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				switchCompiler(
					errorConverter(),
					context(),
					options("square" lineTo type("side")),
					typed(
						switch("circle" caseTo expression(switched.op)),
						type(matchingName lineTo type("circle" lineTo type("radius"))))))
	}
}