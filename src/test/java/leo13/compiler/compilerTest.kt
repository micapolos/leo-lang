package leo13.compiler

import leo.base.assertEqualTo
import leo13.lhs
import leo13.script.lineTo
import leo13.script.script
import leo13.type.*
import leo13.value.*
import kotlin.test.Test
import kotlin.test.assertFails

class CompilerTest {
	@Test
	fun pushPlain() {
		compiler(context(), compiled())
			.unsafePush(script())
			.assertEqualTo(compiler(context(), compiled()))

		compiler(context(), compiled())
			.unsafePush(script("foo"))
			.assertEqualTo(compiler(context(), compiled(script("foo"))))

		compiler(context(), compiled())
			.unsafePush(script("x" lineTo script("zero")))
			.assertEqualTo(compiler(context(), compiled(script("x" lineTo script("zero")))))

		compiler(context(), compiled())
			.unsafePush(script("x" lineTo script("zero"), "y" lineTo script("one")))
			.assertEqualTo(compiler(context(), compiled(script("x" lineTo script("zero"), "y" lineTo script("one")))))
	}

	@Test
	fun pushMeta() {
		compiler(context(), compiled())
			.unsafePush(script("meta"))
			.assertEqualTo(compiler(context(), compiled()))

		compiler(context(), compiled())
			.unsafePush(script("meta" lineTo script("meta")))
			.assertEqualTo(compiler(context(), compiled(script("meta"))))

		compiler(context(), compiled())
			.unsafePush(script("meta" lineTo script("meta" lineTo script("meta"))))
			.assertEqualTo(compiler(context(), compiled(script("meta"))))

		compiler(context(), compiled())
			.unsafePush(script("meta" lineTo script("meta" lineTo script("meta" lineTo script("meta")))))
			.assertEqualTo(compiler(context(), compiled(script("meta" lineTo script("meta")))))
	}

	@Test
	fun pushPrevious() {
		compiler(
			context(),
			compiled(
				expr(op(given())),
				trace(
					type(
						"zero" lineTo type(),
						"one" lineTo type(),
						"two" lineTo type()))))
			.unsafePush("previous" lineTo script())
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							op(given()),
							op(lhs)),
						trace(
							type(
								"zero" lineTo type(),
								"one" lineTo type())))))

		compiler(context(), compiled())
			.unsafePush(script("meta" lineTo script("meta")))
			.assertEqualTo(compiler(context(), compiled(script("meta"))))
	}

	@Test
	fun pushPrevious_emptyLhs() {
		assertFails {
			compiler(context(), compiled()).unsafePush("previous" lineTo script())
		}
	}

	@Test
	fun pushPrevious_nonEmptyRhs() {
		assertFails {
			compiler(
				context(),
				compiled(
					expr(op(given())),
					trace(type("zero" lineTo type()))))
				.unsafePush("previous" lineTo script("foo" lineTo script()))
		}
	}

	@Test
	fun pushGiven() {
		val compiled = compiler(
			context(
				traces(),
				functions(),
				typeBindings(type("zero"), type("one"))),
			compiled())

		compiled
			.unsafePush(script("given"))
			.assertEqualTo(
				compiler(
					context(
						traces(),
						functions(),
						typeBindings(type("zero"), type("one"))),
					compiled(
						expr(op(given())),
						trace(type("one")))))

		// TODO: outside given
	}

	@Test
	fun pushOf() {
		compiler(context(), compiled())
			.unsafePush(script("of" lineTo script()))
			.assertEqualTo(compiler(context(), compiled()))

		compiler(context(), compiled())
			.unsafePush(
				script(
					"zero" lineTo script(),
					"of" lineTo script(
						"zero" lineTo script(),
						"or" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(script("zero")),
						trace(type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))))))

		compiler(context(), compiled())
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
					compiled(script(
						"zero" lineTo script(),
						"of" lineTo script(
							"choice" lineTo script(
								"either" lineTo script("zero"),
								"either" lineTo script("one")))))))
	}

	@Test
	fun pushExists() {
		compiler(context(), compiled())
			.unsafePush(script(
				"zero" lineTo script(),
				"or" lineTo script("one"),
				"exists" lineTo script()))
			.assertEqualTo(
				compiler(
					context(
						traces(trace(type(unsafeChoice("zero" caseTo type(), "one" caseTo type())))),
						functions(),
						typeBindings()),
					compiled()))
	}

	@Test
	fun pushContains() {
		compiler(
			context(),
			compiled(
				expr(op("bit" lineTo expr())),
				trace(type("bit" lineTo type()))))
			.unsafePush(
				"contains" lineTo script(
					"bit" lineTo script(
						"zero" lineTo script(),
						"or" lineTo script(
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					context(
						traces(
							trace(
								type(
									"bit" lineTo type(
										unsafeChoice(
											"zero" caseTo type(),
											"one" caseTo type()))))),
						functions(
							function(
								trace(type("bit" lineTo type())),
								compiled(
									script(
										"bit" lineTo script(
											"zero" lineTo script(),
											"or" lineTo script(
												"one" lineTo script())))))),
						typeBindings()),
					compiled()))
	}

	@Test
	fun pushGives() {
		compiler(
			context(),
			compiled(
				expr(op("zero" lineTo expr())),
				trace(type("zero" lineTo type()))))
			.unsafePush("gives" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context()
						.plus(
							function(
								trace(type("zero" lineTo type())),
								compiled(
									expr(op("one" lineTo expr())),
									trace(type("one" lineTo type()))))),
					compiled()))
	}

	@Test
	fun pushFunctionApplication() {
		compiler(
			context(
				traces(),
				functions(
					function(
						trace(type("zero" lineTo type())),
						compiled(
							expr(op("one" lineTo expr())),
							trace(type("one" lineTo type()))))),
				typeBindings()),
			compiled())
			.unsafePush("zero" lineTo script())
			.assertEqualTo(
				compiler(
					context(
						traces(),
						functions(
							function(
								trace(type("zero" lineTo type())),
								compiled(
									expr(op("one" lineTo expr())),
									trace(type("one" lineTo type()))))),
						typeBindings()),
					compiled(
						expr(
							op(
								value(
									fn(
										valueBindings(),
										expr(op("one" lineTo expr()))))),
							op(call(expr(op("zero" lineTo expr()))))),
						trace(type("one" lineTo type())))))
	}

	@Test
	fun pushGiving() {
		compiler(
			context(),
			compiled(
				expr(op("zero" lineTo expr())),
				trace(type("zero" lineTo type()))))
			.unsafePush("giving" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							op("zero" lineTo expr()),
							op(value(fn(valueBindings(), expr(op("one" lineTo expr())))))),
						trace(
							type(
								arrow(
									type("zero" lineTo type()),
									type("one" lineTo type())))))))
	}

	@Test
	fun pushApply() {
		compiler(
			context(),
			compiled(
				expr(op(value(fn(valueBindings(), expr(op("one" lineTo expr())))))),
				trace(
					type(
						arrow(
							type("zero" lineTo type()),
							type("one" lineTo type()))))))
			.unsafePush("apply" lineTo script("zero" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							op(value(fn(valueBindings(), expr(op("one" lineTo expr()))))),
							op(call(expr(op("zero" lineTo expr()))))),
						trace(type("one" lineTo type())))))
	}
}
