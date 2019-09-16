package leo13.untyped.compiler

import leo.base.assertEqualTo
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.expression.*
import leo13.untyped.pattern.*
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
						expression(
							"zero" lineTo expression(),
							"plus" lineTo expression("one")),
						pattern(
							"zero" lineTo pattern(),
							"plus" lineTo pattern("one")))))
	}

	@Test
	fun get() {
		compiler()
			.process(token(opening("color")))
			.process(token(opening("circle")))
			.process(token(opening("color")))
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().set(
					compiled(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression(
									"red" lineTo expression())))
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
								"color" lineTo expression(
									"red" lineTo expression())))
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
	fun everything() {
		compiler()
			.process(token(opening("everything")))
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
							.plus(everything.op),
						pattern(
							"x" lineTo pattern("zero"),
							"y" lineTo pattern("one")))))
	}

	@Test
	fun given() {
		compiler()
			.set(context().bind(pattern("zero")))
			.process(token(opening("given")))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(context().bind(pattern("zero")))
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
			.process(token(opening("choice")))
			.process(token(opening("either")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("either")))
			.process(token(opening("one")))
			.process(token(closing))
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
			.process(token(opening("bit")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("switch")))
			.process(token(opening("case")))
			.process(token(opening("zero")))
			.process(token(opening("and")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.set(
						compiled(
							expression("bit" lineTo expression("zero"))
								.plus(switch("zero" caseTo expression("and" lineTo expression("one"))).op),
							pattern("bit" lineTo pattern("zero"), "and" lineTo pattern("one")))))
	}
}
