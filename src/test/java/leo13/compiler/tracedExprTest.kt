package leo13.compiler

import leo.base.assertEqualTo
import leo13.lhs
import leo13.rhs
import leo13.type.lineTo
import leo13.type.recursion
import leo13.type.thunk
import leo13.type.type
import leo13.value.expr
import leo13.value.lineTo
import leo13.value.op
import kotlin.test.Test

class TracedExprTest {
	@Test
	fun lhsOrNull() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo type())))
			.lhsOrNull
			.assertEqualTo(
				traced(
					expr(
						op("expr" lineTo expr()),
						op(lhs)),
					trace(
						type("type"),
						type("one" lineTo type()))))
	}

	@Test
	fun lhsOrNull_noLhs() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("foo"),
				type()))
			.lhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_noRhs() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type()))
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_type() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo type("three"))))
			.rhsOrNull
			.assertEqualTo(
				traced(
					expr(
						op("expr" lineTo expr()),
						op(rhs)),
					trace(
						type("type"),
						type(
							"one" lineTo type(),
							"two" lineTo type("three")),
						type("three"))))
	}

	@Test
	fun rhsOrNull_recursion() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo thunk(recursion))))
			.rhsOrNull
			.assertEqualTo(
				traced(
					expr(
						op("expr" lineTo expr()),
						op(rhs)),
					trace(type("type"))))
	}

	@Test
	fun rhsOrNull_invalidRecursion() {
		traced(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo thunk(recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(null)
	}
}