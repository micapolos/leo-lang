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
				expr(given()),
				type(
					pattern(
						"zero" lineTo pattern(),
						"one" lineTo pattern(),
						"two" lineTo pattern()))))
			.unsafePush("previous" lineTo script())
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							given(),
							op(lhs)),
						type(
							pattern(
								"zero" lineTo pattern(),
								"one" lineTo pattern())))))

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
					type(pattern("zero" lineTo pattern()))))
				.unsafePush("previous" lineTo script("foo" lineTo script()))
		}
	}

	@Test
	fun pushGiven() {
		val compiled = compiler(
			context(
				types(),
				functions(),
				typeBindings(pattern("zero"), pattern("one"))),
			compiled())

		compiled
			.unsafePush(script("given"))
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(),
						typeBindings(pattern("zero"), pattern("one"))),
					compiled(
						expr(given()),
						type(pattern("one")))))

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
						type(pattern(unsafeChoice("zero" caseTo pattern(), "one" caseTo pattern()))))))

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
						types(type(pattern(unsafeChoice("zero" caseTo pattern(), "one" caseTo pattern())))),
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
				type(pattern("bit" lineTo pattern()))))
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
								pattern(
									"bit" lineTo pattern(
										unsafeChoice(
											"zero" caseTo pattern(),
											"one" caseTo pattern()))))),
						functions(
							function(
								type(pattern("bit" lineTo pattern())),
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
				type(pattern("zero" lineTo pattern()))))
			.unsafePush("gives" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context()
						.plus(
							function(
								type(pattern("zero" lineTo pattern())),
								compiled(
									expr(given()),
									type(pattern("one" lineTo pattern()))))),
					compiled()))
	}

	@Test
	fun pushFunctionApplication() {
		compiler(
			context(
				types(),
				functions(
					function(
						type(pattern("zero" lineTo pattern())),
						compiled(
							expr(given()),
							type(pattern("one" lineTo pattern()))))),
				typeBindings()),
			compiled())
			.unsafePush("zero" lineTo script())
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(
							function(
								type(pattern("zero" lineTo pattern())),
								compiled(
									expr(value(), op("one" lineTo expr())),
									type(pattern("one" lineTo pattern()))))),
						typeBindings()),
					compiled(
						expr(
							value(
								fn(
									valueBindings(),
									expr(value(), op("one" lineTo expr())))),
							op(call(expr(value(), op("zero" lineTo expr()))))),
						type(pattern("one" lineTo pattern())))))
	}

	@Test
	fun pushGiving() {
		compiler(
			context(),
			compiled(
				expr(value(), op("zero" lineTo expr())),
				type(pattern("zero" lineTo pattern()))))
			.unsafePush("giving" lineTo script("one" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(value(fn(valueBindings(), expr(value(), op("one" lineTo expr()))))),
						type(
							pattern(
								arrow(
									pattern("zero" lineTo pattern()),
									pattern("one" lineTo pattern())))))))
	}

	@Test
	fun pushApply() {
		compiler(
			context(),
			compiled(
				expr(value(fn(valueBindings(), expr(value(), op("one" lineTo expr()))))),
				type(
					pattern(
						arrow(
							pattern("zero" lineTo pattern()),
							pattern("one" lineTo pattern()))))))
			.unsafePush("apply" lineTo script("zero" lineTo script()))
			.assertEqualTo(
				compiler(
					context(),
					compiled(
						expr(
							value(fn(valueBindings(), expr(value(), op("one" lineTo expr())))),
							op(call(expr(value(), op("zero" lineTo expr()))))),
						type(pattern("one" lineTo pattern())))))
	}
}
