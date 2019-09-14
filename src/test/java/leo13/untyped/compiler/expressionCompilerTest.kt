package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.*
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
							.plus(get("color").op),
						pattern("color" lineTo pattern("red")))))
	}

	@Test
	fun bind() {
		expressionCompiler()
			.set(compiled(expression("zero"), pattern("zero")))
			.process(token(opening("in")))
			.process(token(opening("given")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression("zero")
							.plus(bind(expression(given.op)).op),
						pattern("given" lineTo pattern("zero")))))
	}

	@Test
	fun previous() {
		expressionCompiler()
			.process(token(opening("previous")))
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler().set(
					compiled(
						expression(
							"x" lineTo expression("zero"),
							"y" lineTo expression("one"))
							.plus(previous.op),
						pattern("x" lineTo pattern("zero")))))
	}

	@Test
	fun given() {
		expressionCompiler()
			.set(context().bind(pattern("zero")))
			.process(token(opening("given")))
			.process(token(closing))
			.assertEqualTo(
				expressionCompiler()
					.set(context().bind(pattern("zero")))
					.set(
						compiled(
							expression(given.op),
							pattern("given" lineTo pattern("zero")))))
	}
}
