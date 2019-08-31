package leo13.compiler

import leo.base.assertEqualTo
import leo13.lhs
import leo13.rhs
import leo13.rhsLine
import leo13.type.*
import leo13.value.expr
import leo13.value.given
import leo13.value.op
import leo13.value.value
import kotlin.test.Test

class CompiledTest {
	@Test
	fun lhsOrNull() {
		compiled(
			expr(value("foo")),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo pattern())))
			.lhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						value("foo"),
						op(lhs)),
					type(
						pattern("type"),
						pattern("one" lineTo pattern()))))
	}

	@Test
	fun lhsOrNull_noLhs() {
		compiled(
			expr(value("foo")),
			type(
				pattern("foo"),
				pattern()))
			.lhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_noRhs() {
		compiled(
			expr(value("foo")),
			type(
				pattern("type"),
				pattern()))
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_type() {
		compiled(
			expr(value("foo")),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo pattern("three"))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						value("foo"),
						op(rhs)),
					type(
						pattern("type"),
						pattern(
							"one" lineTo pattern(),
							"two" lineTo pattern("three")),
						pattern("three"))))
	}

	@Test
	fun rhsOrNull_recursion() {
		compiled(
			expr(value("foo")),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo thunk(recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						value("foo"),
						op(rhs)),
					type(pattern("type"))))
	}

	@Test
	fun rhsOrNull_invalidRecursion() {
		compiled(
			expr(value("foo")),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo thunk(recursion.recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun stringLineOrNull() {
		compiled(
			expr(given()),
			type(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one"))))
			.let { compiled ->
				compiled
					.lineOrNull("x")
					.assertEqualTo(
						compiled(
							expr(given(), op(lhs), op(rhsLine)),
							type(pattern("x" lineTo pattern("zero")))))

				compiled
					.lineOrNull("y")
					.assertEqualTo(
						compiled(
							expr(given(), op(rhsLine)),
							type(pattern("y" lineTo pattern("one")))))

				compiled
					.lineOrNull("z")
					.assertEqualTo(null)
			}
	}

	@Test
	fun accessOrNull() {
		compiled(
			expr(given()),
			type(
				pattern(
					"vec" lineTo pattern(
						"x" lineTo pattern("zero"),
						"y" lineTo pattern("one")))))
			.accessOrNull("x")
			.assertEqualTo(
				compiled(
					expr(given(), op(rhs), op(lhs), op(rhsLine)),
					type(
						pattern(
							"vec" lineTo pattern(
								"x" lineTo pattern("zero"),
								"y" lineTo pattern("one"))),
						pattern("x" lineTo pattern("zero")))))
	}
}