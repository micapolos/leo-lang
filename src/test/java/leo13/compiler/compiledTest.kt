package leo13.compiler

import leo.base.assertEqualTo
import leo13.lhs
import leo13.rhs
import leo13.type.*
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import kotlin.test.Test

class CompiledTest {
	@Test
	fun lhsOrNull() {
		compiled(
			expr(op("expr" lineTo expr())),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo pattern())))
			.lhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						op("expr" lineTo expr()),
						op(lhs)),
					type(
						pattern("type"),
						pattern("one" lineTo pattern()))))
	}

	@Test
	fun lhsOrNull_noLhs() {
		compiled(
			expr(op("expr" lineTo expr())),
			type(
				pattern("foo"),
				pattern()))
			.lhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_noRhs() {
		compiled(
			expr(op("expr" lineTo expr())),
			type(
				pattern("type"),
				pattern()))
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_type() {
		compiled(
			expr(op("expr" lineTo expr())),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo pattern("three"))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						op("expr" lineTo expr()),
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
			expr(op("expr" lineTo expr())),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo thunk(recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						op("expr" lineTo expr()),
						op(rhs)),
					type(pattern("type"))))
	}

	@Test
	fun rhsOrNull_invalidRecursion() {
		compiled(
			expr(op("expr" lineTo expr())),
			type(
				pattern("type"),
				pattern(
					"one" lineTo pattern(),
					"two" lineTo thunk(recursion.recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(null)
	}
}