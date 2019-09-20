package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.contentName
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.*
import leo13.untyped.pattern.*
import leo13.untyped.value.function
import leo13.untyped.value.item
import leo13.untyped.value.value
import org.junit.Test

class CompilerTest {
	@Test
	fun lines() {
		compiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("plus")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression("zero").plus("plus" lineTo expression("one")),
						pattern(
							"zero" lineTo pattern(),
							"plus" lineTo pattern("one")))))
	}

	@Test
	fun get() {
		compiler()
			.process(token(opening("circle")))
			.process(token(opening("color")))
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("color")))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression("red")))
							.plus(get("color").op),
						pattern("color" lineTo pattern("red")))))
	}

	@Test
	fun set() {
		compiler()
			.process(token(opening("circle")))
			.process(token(opening("color")))
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("set")))
			.process(token(opening("color")))
			.process(token(opening("blue")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression("red")))
							.plus(set("color" lineTo expression("blue")).op),
						pattern("circle" lineTo pattern("color" lineTo pattern("blue"))))))
	}

	@Test
	fun bind() {
		compiler()
			.set(compiled(expression("zero"), pattern("zero")))
			.process(token(opening("in")))
			.process(token(opening("given")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression("zero")
							.plus(bind(expression(given.op)).op),
						pattern("given" lineTo pattern("zero")))))
	}

	@Test
	fun previous() {
		compiler()
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
				compiler().set(
					compiled(
						expression(
							"x" lineTo expression("zero"),
							"y" lineTo expression("one"))
							.plus(previous.op),
						pattern("x" lineTo pattern("zero")))))
	}

	@Test
	fun content() {
		compiler()
			.process(token(opening(contentName)))
			.process(token(opening("vec")))
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression(
							"vec" lineTo expression(
								"x" lineTo expression("zero"),
								"y" lineTo expression("one")))
							.plus(content.op),
						pattern(
							"x" lineTo pattern("zero"),
							"y" lineTo pattern("one")))))
	}

	@Test
	fun given() {
		compiler()
			.set(context().give(pattern("zero")))
			.process(token(opening("given")))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(context().give(pattern("zero")))
					.set(
						compiled(
							expression(given.op),
							pattern("given" lineTo pattern("zero")))))
	}

	@Test
	fun processOf() {
		compiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("of")))
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(
						compiled(
							expression("zero"),
							pattern(item(choice(either("zero"), either("one")))))))
	}

	@Test
	fun processSwitch() {
		compiler()
			.process(token(opening("shape")))
			.process(token(opening("circle")))
			.process(token(opening("radius")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("switch")))
			.process(token(opening("circle")))
			.process(token(opening("switched")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(
						compiled(
							expression("shape" lineTo expression("circle" lineTo expression("radius")))
								.plus(switch("circle" caseTo expression(switched.op)).op),
							pattern("switched" lineTo pattern("circle" lineTo pattern("radius"))))))
	}

	@Test
	fun processGives() {
		compiler()
			.process(token(opening("function")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("gives")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(
						compiled(
							expression(value(item(function(valueContext(), expression("one")))).op),
							pattern(item(pattern("zero") arrowTo pattern("one"))))))
	}

	@Test
	fun processAs() {
		compiler()
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(opening("as")))
			.process(token(opening("color")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(
						compiled(
							expression("red").plus(wrap("color").op),
							pattern("color" lineTo pattern("red")))))
	}
}
