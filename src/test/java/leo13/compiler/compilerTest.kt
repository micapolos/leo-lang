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
	fun pushGet() {
		compiler(
			context(),
			compiled(
				expr(given()),
				type(
					"vec" lineTo type(
						"x" lineTo type("zero"),
						"y" lineTo type("one")))))
			.let { compiler ->
				compiler
					.unsafePush("x" lineTo script())
					.assertEqualTo(
						compiler(
							context(),
							compiler.compiled.getOrNull("x")!!))
			}
	}

	@Test
	fun pushSet() {
		val compiler = compiler(
			context(),
			compiled(
				expr(given()),
				type(
					"vec" lineTo type(
						"x" lineTo type("zero"),
						"y" lineTo type("one")))))

		compiler
			.unsafePush(
				"set" lineTo script(
					"x" lineTo script("ten"),
					"y" lineTo script("eleven")))
			.assertEqualTo(
				compiler
					.unsafePushSet("x" lineTo script("ten"))
					.unsafePushSet("y" lineTo script("eleven")))
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
				expr(given()),
				type(
					"zero" lineTo type(),
					"one" lineTo type(),
					"two" lineTo type())))
			.unsafePush("previous" lineTo script())
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							given(),
							op(lhs)),
						type(
							"zero" lineTo type(),
							"one" lineTo type()))))

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
					expr(given()),
					type("zero" lineTo type())))
				.unsafePush("previous" lineTo script("foo" lineTo script()))
		}
	}

	@Test
	fun pushGiven() {
		val compiled = compiler(
			context(
				types(),
				functions(),
				typeBindings(type("zero"), type("one"))),
			compiled())

		compiled
			.unsafePush(script("given"))
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(),
						typeBindings(type("zero"), type("one"))),
					compiled(
						expr(given()),
						type("one"))))

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
						type(unsafeChoice("zero" caseTo type(), "one" caseTo type())))))

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
						types(type(unsafeChoice("zero" caseTo type(), "one" caseTo type()))),
						functions(),
						typeBindings()),
					compiled()))
	}

	@Test
	fun pushContains() {
		compiler(
			context(),
			compiled(
				expr(given()),
				type("bit" lineTo type())))
			.unsafePush(
				"contains" lineTo script(
					"bit" lineTo script(
						"zero" lineTo script(),
						"or" lineTo script(
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					context(
						types(
							type(
								"bit" lineTo type(
									unsafeChoice(
										"zero" caseTo type(),
										"one" caseTo type())))),
						functions(
							function(
								type("bit" lineTo type()),
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
				expr(given()),
				type("zero" lineTo type())))
			.unsafePush("gives" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context()
						.plus(
							function(
								type("zero" lineTo type()),
								compiled(
									expr(op("one" lineTo expr())),
									type("one" lineTo type())))),
					compiled()))
	}

	@Test
	fun pushFunctionApplication() {
		compiler(
			context(
				types(),
				functions(
					function(
						type("zero" lineTo type()),
						compiled(
							expr(given()),
							type("one" lineTo type())))),
				typeBindings()),
			compiled())
			.unsafePush("zero" lineTo script())
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(
							function(
								type("zero" lineTo type()),
								compiled(
									expr(given()),
									type("one" lineTo type())))),
						typeBindings()),
					compiled(
						expr(
							value(
								fn(
									valueBindings(),
									expr(given()))),
							op(call(expr(value(), op("zero" lineTo expr()))))),
						type("one" lineTo type()))))
	}

	@Test
	fun pushGiving() {
		compiler(
			context(),
			compiled(
				expr(value(), op("zero" lineTo expr())),
				type("zero" lineTo type())))
			.unsafePush("giving" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(value(fn(valueBindings(), expr(value(), op("one" lineTo expr()))))),
						type(
							arrow(
								type("zero" lineTo type()),
								type("one" lineTo type()))))))
	}

	@Test
	fun pushApply() {
		compiler(
			context(),
			compiled(
				expr(value(fn(valueBindings(), expr(value(), op("one" lineTo expr()))))),
				type(
					arrow(
						type("zero" lineTo type()),
						type("one" lineTo type())))))
			.unsafePush("apply" lineTo script("zero" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							value(fn(valueBindings(), expr(value(), op("one" lineTo expr())))),
							op(call(expr(value(), op("zero" lineTo expr()))))),
						type("one" lineTo type()))))
	}
}
