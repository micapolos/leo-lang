package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.expression
import leo13.untyped.expression.lineTo
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.lineTo
import leo13.untyped.pattern.pattern
import org.junit.Test

class ExpressionCompilerTest {
	@Test
	fun lines() {
		expressionCompiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("plus")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression(
							"zero" lineTo expression(),
							"plus" lineTo expression("one")),
						pattern(
							"zero" lineTo pattern(),
							"plus" lineTo pattern("one")))))
	}

	@Test
	fun normalization() {
		expressionCompiler()
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(opening("color")))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression("color" lineTo expression("red")),
						pattern("color" lineTo pattern("red")))))
	}


	@Test
	fun get() {
		expressionCompiler()
			.process(token(opening("color")))
			.process(token(opening("circle")))
			.process(token(opening("color")))
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression(
									"red" lineTo expression())))
							.plus(leo13.untyped.expression.get("color").op),
						pattern("color" lineTo pattern("red")))))
	}
}
