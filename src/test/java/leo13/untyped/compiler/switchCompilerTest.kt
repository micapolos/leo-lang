package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.*
import leo13.untyped.pattern.eitherTo
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import leo9.stack
import kotlin.test.Test

class SwitchCompilerTest {
	@Test
	fun process() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			stack(
				"square" eitherTo pattern("side"),
				"circle" eitherTo pattern("radius")),
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
					stack("square" eitherTo pattern("side")),
					compiled(
						switch("circle" caseTo expression("circled")),
						pattern("circled"))))
	}

	@Test
	fun processSwitched() {
		val switchCompiler = switchCompiler(
			errorConverter(),
			context(),
			stack(
				"square" eitherTo pattern("side"),
				"circle" eitherTo pattern("radius")),
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
					stack("square" eitherTo pattern("side")),
					compiled(
						switch("circle" caseTo expression(switched.op)),
						pattern("switched" lineTo pattern("circle" lineTo pattern("radius"))))))
	}
}