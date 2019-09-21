package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.expression.*
import leo13.pattern.lineTo
import leo13.pattern.options
import leo13.pattern.pattern
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class SwitchCompilerTest {
	@Test
	fun process() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			options(
				"square" lineTo pattern("side"),
				"circle" lineTo pattern("radius")),
			compiled(switch(), pattern("lhs")))

		switchCompiler
			.process(token(opening("circle")))
			.process(token(opening("circled")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				switchCompiler(
					errorConverter(),
					context(),
					options("square" lineTo pattern("side")),
					compiled(
						switch("circle" caseTo expression("circled")),
						pattern("circled"))))
	}

	@Test
	fun processSwitched() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			options(
				"square" lineTo pattern("side"),
				"circle" lineTo pattern("radius")),
			compiled(switch(), pattern("lhs")))

		switchCompiler
			.process(token(opening("circle")))
			.process(token(opening("switched")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				switchCompiler(
					errorConverter(),
					context(),
					options("square" lineTo pattern("side")),
					compiled(
						switch("circle" caseTo expression(switched.op)),
						pattern("switched" lineTo pattern("circle" lineTo pattern("radius"))))))
	}
}