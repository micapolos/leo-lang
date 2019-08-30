package leo13.compiler

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.type.*
import leo13.value.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun pushPlain() {
		compiler(context(), typed())
			.unsafePush(script())
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.unsafePush(script("foo"))
			.assertEqualTo(compiler(context(), typed(script("foo"))))

		compiler(context(), typed())
			.unsafePush(script("x" lineTo script("zero")))
			.assertEqualTo(compiler(context(), typed(script("x" lineTo script("zero")))))

		compiler(context(), typed())
			.unsafePush(script("x" lineTo script("zero"), "y" lineTo script("one")))
			.assertEqualTo(compiler(context(), typed(script("x" lineTo script("zero"), "y" lineTo script("one")))))
	}

	@Test
	fun pushMeta() {
		compiler(context(), typed())
			.unsafePush(script("meta"))
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.unsafePush(script("meta" lineTo script("meta")))
			.assertEqualTo(compiler(context(), typed(script("meta"))))

		compiler(context(), typed())
			.unsafePush(script("meta" lineTo script("meta" lineTo script("meta"))))
			.assertEqualTo(compiler(context(), typed(script("meta"))))

		compiler(context(), typed())
			.unsafePush(script("meta" lineTo script("meta" lineTo script("meta" lineTo script("meta")))))
			.assertEqualTo(compiler(context(), typed(script("meta" lineTo script("meta")))))
	}

	@Test
	fun pushGiven() {
		val compiled = compiler(
			context(
				types(),
				functions(),
				typeBindings(type("zero"), type("one"))),
			typed())

		compiled
			.unsafePush(script("given"))
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(),
						typeBindings(type("zero"), type("one"))),
					typed(
						expr(op(given())),
						type("one"))))

		// TODO: outside given
	}

	@Test
	fun pushOf() {
		compiler(context(), typed())
			.unsafePush(script("of" lineTo script()))
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.unsafePush(
				script(
					"zero" lineTo script(),
					"of" lineTo script(
						"zero" lineTo script(),
						"or" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					context(),
					typed(
						expr(script("zero")),
						type(unsafeChoice("zero" caseTo type(), "one" caseTo type())))))

		compiler(context(), typed())
			.unsafePush(script(
				"zero" lineTo script(),
				"meta" lineTo script(
					"of" lineTo script(
						"choice" lineTo script(
							"either" lineTo script("zero"),
							"either" lineTo script("one"))))))
			.assertEqualTo(
				compiler(
					context(),
					typed(script(
						"zero" lineTo script(),
						"of" lineTo script(
							"choice" lineTo script(
								"either" lineTo script("zero"),
								"either" lineTo script("one")))))))
	}

	@Test
	fun pushExists() {
		compiler(context(), typed())
			.unsafePush(script(
				"zero" lineTo script(),
				"or" lineTo script("one"),
				"exists" lineTo script()))
			.assertEqualTo(
				compiler(
					context(
						types(type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))),
						functions(),
						typeBindings()),
					typed(
						expr(op(value())),
						type())))
	}

	@Test
	fun pushGives() {
		compiler(
			context(),
			typed(
				expr(op("zero" lineTo expr())),
				type("zero" lineTo type())))
			.unsafePush("gives" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context()
						.plus(
							function(
								type("zero" lineTo type()),
								typed(
									expr(op("one" lineTo expr())),
									type("one" lineTo type())))),
					typed(
						expr(
							op("zero" lineTo expr()),
							op(value())),
						type())))
	}

	@Test
	fun pushGiving() {
		compiler(
			context(),
			typed(
				expr(op("zero" lineTo expr())),
				type("zero" lineTo type())))
			.unsafePush("giving" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					typed(
						expr(
							op("zero" lineTo expr()),
							op(value(fn(valueBindings(), expr(op("one" lineTo expr())))))),
						type(
							arrow(
								type("zero" lineTo type()),
								type("one" lineTo type()))))))
	}

	@Test
	fun pushApply() {
		compiler(
			context(),
			typed(
				expr(op(value(fn(valueBindings(), expr(op("one" lineTo expr())))))),
				type(
					arrow(
						type("zero" lineTo type()),
						type("one" lineTo type())))))
			.unsafePush("apply" lineTo script("zero" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					typed(
						expr(
							op(value(fn(valueBindings(), expr(op("one" lineTo expr()))))),
							op(call(expr(op("zero" lineTo expr()))))),
						type("one" lineTo type()))))
	}
}
