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
				"circle" eitherTo pattern("radius"),
				"square" eitherTo pattern("side")),
			compiled(switch(), pattern("lhs")))

		switchCompiler
			.process(token(opening("case")))
			.process(token(opening("square")))
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				switchCompiler(
					errorConverter(),
					context(),
					stack("circle" eitherTo pattern("radius")),
					compiled(
						switch(
							"square" caseTo expression(plus("foo" lineTo expression()).op)),
						pattern(
							"foo" lineTo pattern("side")))))
	}
}