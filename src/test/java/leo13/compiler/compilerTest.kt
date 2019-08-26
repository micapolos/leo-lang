package leo13.compiler

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.type.*
import leo13.value.expr
import leo13.value.given
import leo13.value.op
import kotlin.test.Test

class CompilerTest {
	@Test
	fun pushPlain() {
		compiler(context(), typed())
			.push(script())
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.push(script("foo"))
			.assertEqualTo(compiler(context(), typed(script("foo"))))

		compiler(context(), typed())
			.push(script("x" lineTo script("zero")))
			.assertEqualTo(compiler(context(), typed(script("x" lineTo script("zero")))))

		compiler(context(), typed())
			.push(script("x" lineTo script("zero"), "y" lineTo script("one")))
			.assertEqualTo(compiler(context(), typed(script("x" lineTo script("zero"), "y" lineTo script("one")))))
	}

	@Test
	fun pushMeta() {
		compiler(context(), typed())
			.push(script("meta"))
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.push(script("meta" lineTo script("meta")))
			.assertEqualTo(compiler(context(), typed(script("meta"))))

		compiler(context(), typed())
			.push(script("meta" lineTo script("meta" lineTo script("meta"))))
			.assertEqualTo(compiler(context(), typed(script("meta"))))

		compiler(context(), typed())
			.push(script("meta" lineTo script("meta" lineTo script("meta" lineTo script("meta")))))
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
			.push(script("given"))
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
			.push(script("of" lineTo script()))
			.assertEqualTo(compiler(context(), typed()))

		compiler(context(), typed())
			.push(script(
				"zero" lineTo script(),
				"of" lineTo script(
					"choice" lineTo script(
						"either" lineTo script("zero"),
						"either" lineTo script("one")))))
			.assertEqualTo(
				compiler(
					context(),
					typed(
						expr(script("zero")),
						type(unsafeChoice(either("zero"), either("one"))))))

		compiler(context(), typed())
			.push(script(
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
			.push(script(
				"choice" lineTo script(
					"either" lineTo script("zero"),
					"either" lineTo script("one")),
				"exists" lineTo script()))
			.assertEqualTo(
				compiler(
					context(
						types(type(unsafeChoice(either("zero"), either("one")))),
						functions(),
						typeBindings()),
					typed()))

	}

	@Test
	fun pushGives() {
		compiler(context(), typed())
			.push(script(
				"choice" lineTo script(
					"either" lineTo script("zero"),
					"either" lineTo script("one")),
				"gives" lineTo script("given")))
			.assertEqualTo(
				compiler(
					context(
						types(),
						functions(
							function(
								type(unsafeChoice(either("zero"), either("one"))),
								typed(
									expr(op(given())),
									type(unsafeChoice(either("zero"), either("one")))))),
						typeBindings()),
					typed()))
	}
}
