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
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo type())))
			.lhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						op("expr" lineTo expr()),
						op(lhs)),
					trace(
						type("type"),
						type("one" lineTo type()))))
	}

	@Test
	fun lhsOrNull_noLhs() {
		compiled(
			expr(op("expr" lineTo expr())),
			trace(
				type("foo"),
				type()))
			.lhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_noRhs() {
		compiled(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type()))
			.rhsOrNull
			.assertEqualTo(null)
	}

	@Test
	fun rhsOrNull_type() {
		compiled(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo type("three"))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
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
		compiled(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo thunk(recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(
				compiled(
					expr(
						op("expr" lineTo expr()),
						op(rhs)),
					trace(type("type"))))
	}

	@Test
	fun rhsOrNull_invalidRecursion() {
		compiled(
			expr(op("expr" lineTo expr())),
			trace(
				type("type"),
				type(
					"one" lineTo type(),
					"two" lineTo thunk(recursion.recursion.recursion))))
			.rhsOrNull
			.assertEqualTo(null)
	}
}