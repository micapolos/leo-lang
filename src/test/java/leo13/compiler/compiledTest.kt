package leo13.compiler

import leo.base.assertEqualTo
import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.type.lineTo
import leo13.type.type
import leo13.value.*
import kotlin.test.Test

class CompiledTest {
	@Test
	fun lhsOrNull() {
		compiled(
			expr(value("foo")),
			type(
				"one" lineTo type(),
				"two" lineTo type()))
			.lhsOrNull
			.assertEqualTo(
				compiled(
					expr(value("foo"), op(lhs)),
					type("one" lineTo type())))
	}

	@Test
	fun lhsOrNull_noLhs() {
		compiled(
			expr(value("foo")),
			type())
			.lhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_noRhs() {
		compiled(
			expr(value("foo")),
			type())
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_type() {
		compiled(
			expr(value("foo")),
			type(
				"one" lineTo type(),
				"two" lineTo type("three")))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						value("foo"),
						op(rhs)),
					type("three")))
	}

	@Test
	fun stringLineOrNull() {
		compiled(
			expr(given()),
			type(
				"x" lineTo type("zero"),
				"y" lineTo type("one")))
			.let { compiled ->
				compiled
					.lineOrNull("x")
					.assertEqualTo(
						compiled(
							expr(given(), op(lhs), op(rhsLine)),
							type("x" lineTo type("zero"))))

				compiled
					.lineOrNull("y")
					.assertEqualTo(
						compiled(
							expr(given(), op(rhsLine)),
							type("y" lineTo type("one"))))

				compiled
					.lineOrNull("z")
					.assertEqualTo(null)
			}
	}

	@Test
	fun getOrNull() {
		compiled(
			expr(given()),
			type(
				"vec" lineTo type(
					"x" lineTo type("zero"),
					"y" lineTo type("one"))))
			.getOrNull("x")
			.assertEqualTo(
				compiled(
					expr(given(), op(leo13.value.get("x"))),
					type("x" lineTo type("zero"))))
	}

	@Test
	fun setOrNull() {
		compiled(
			expr(given()),
			type(
				"vec" lineTo type(
					"x" lineTo type("zero"),
					"y" lineTo type("one"))))
			.setOrNull("x" lineTo compiled(expr(value("eleven")), type("eleven")))
			.assertEqualTo(
				compiled(
					expr(given(), op(set("x" lineTo expr(value("eleven"))))),
					type(
						"vec" lineTo type(
							"x" lineTo type("eleven"),
							"y" lineTo type("one")))))
	}
}