package leo13.compiler

import leo.base.assertEqualTo
import leo13.applyName
import leo13.contentName
import leo13.expression.*
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.type.arrowTo
import leo13.type.lineTo
import leo13.type.options
import leo13.type.type
import leo13.value.function
import leo13.value.item
import leo13.value.value
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
				compiler().process(
					typed(
						expression("zero").plus("plus" lineTo expression("one")),
						type(
							"zero" lineTo type(),
							"plus" lineTo type("one")))))
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
				compiler().process(
					typed(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression("red")))
							.plus(get("color").op),
						type("color" lineTo type("red")))))
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
			.process(token(opening("red")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().process(
					typed(
						expression(
							"circle" lineTo expression(
								"color" lineTo expression("red")))
							.plus(set("color" lineTo expression("red")).op),
						type("circle" lineTo type("color" lineTo type("red"))))))
	}

	@Test
	fun bind() {
		compiler()
			.copy(compiled = compiled(context(), typed(expression("zero"), type("zero"))))
			.process(token(opening("in")))
			.process(token(opening("given")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler().process(
					typed(
						expression("zero")
							.plus(bind(expression(leo13.given.op)).op),
						type("given" lineTo type("zero")))))
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
				compiler().process(
					typed(
						expression(
							"x" lineTo expression("zero"),
							"y" lineTo expression("one"))
							.plus(previous.op),
						type("x" lineTo type("zero")))))
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
				compiler().process(
					typed(
						expression(
							"vec" lineTo expression(
								"x" lineTo expression("zero"),
								"y" lineTo expression("one")))
							.plus(content.op),
						type(
							"x" lineTo type("zero"),
							"y" lineTo type("one")))))
	}

	@Test
	fun given() {
		compiler()
			.copy(compiled = compiled(context().give(type("zero"))))
			.process(token(opening("given")))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.copy(compiled = compiled(context().give(type("zero"))))
					.process(
						typed(
							expression(leo13.given.op),
							type("given" lineTo type("zero")))))
	}

	@Test
	fun processOf() {
		compiler()
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("of")))
			.process(token(opening("options")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.process(
						typed(
							expression("zero"),
							type(options("zero", "one")))))
	}

	@Test
	fun processMatch() {
		compiler()
			.copy(compiled = compiled(context(),
				typed(
					expression(op(value("foo"))),
					type("bit" lineTo type(options("zero", "one"))))))
			.process(token(opening("match")))
			.process(token(opening("zero")))
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("one")))
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.process(
						typed(
							expression(
								op(value("foo")),
								op(switch(
									"zero" caseTo expression("foo"),
									"one" caseTo expression("foo")))),
							type("foo"))))
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
					.process(
						typed(
							expression(value(item(function(valueContext(), expression("one")))).op),
							type(type("zero") arrowTo type("one")))))
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
					.process(
						typed(
							expression("red").plus(wrap("color").op),
							type("color" lineTo type("red")))))
	}

	@Test
	fun addWord() {
		compiler()
			.process(token(opening("bit")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("negate")))
			.process(token(closing))
			.assertEqualTo(compiler()
				.process(
					typed(
						expression(
							"bit" lineTo expression("zero"),
							"negate" lineTo expression()),
						type(
							"bit" lineTo type("zero"),
							"negate" lineTo type()))))
	}

	@Test
	fun processApply() {
		val compiler =
			compiler()
				.process(compiled(
					context(),
					typed(
						expression("foo"),
						type("writer" lineTo type(type("zero") arrowTo type("one"))))))

		compiler
			.process(token(opening(applyName)))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				compiler()
					.process(
						compiled(
							context(),
							typed(
								expression("foo").plus(op(apply(expression("zero")))),
								type("one")))))
	}
}
