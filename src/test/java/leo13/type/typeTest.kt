package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTest {
	@Test
	fun applyThunk() {
		type(pattern("one"), pattern("two"))
			.applyOrNull(rhs(pattern("three")))
			.assertEqualTo(type(pattern("one"), pattern("two"), pattern("three")))

		type(pattern("one"), pattern("two"))
			.applyOrNull(rhs(recursion))
			.assertEqualTo(type(pattern("one"), pattern("two")))

		type(pattern("one"), pattern("two"))
			.applyOrNull(rhs(recursion.recursion))
			.assertEqualTo(type(pattern("one")))

		type(pattern("one"), pattern("two"))
			.applyOrNull(rhs(recursion.recursion.recursion))
			.assertEqualTo(null)
	}

	@Test
	fun lhsOrNull() {
		type(
			pattern("foo"),
			pattern())
			.lhsOrNull
			.assertEqualTo(null)

		type(
			pattern("foo"),
			pattern("two"))
			.lhsOrNull
			.assertEqualTo(
				type(
					pattern("foo"),
					pattern()))

		type(
			pattern("foo"),
			pattern(
				"one" lineTo pattern(),
				"two" lineTo pattern()))
			.lhsOrNull
			.assertEqualTo(
				type(
					pattern("foo"),
					pattern("one")))
	}

	@Test
	fun rhsOrNull() {
		type(
			pattern("foo"),
			pattern())
			.rhsOrNull
			.assertEqualTo(null)

		type(
			pattern("foo"),
			pattern("one"))
			.rhsOrNull
			.assertEqualTo(
				type(
					pattern("foo"),
					pattern("one"),
					pattern()))

		type(
			pattern("foo"),
			pattern("one" lineTo pattern("two")))
			.rhsOrNull
			.assertEqualTo(
				type(
					pattern("foo"),
					pattern("one" lineTo pattern("two")),
					pattern("two")))
	}

	@Test
	fun nameRhsOrNull() {
		type(pattern("one"), pattern("two" lineTo pattern("three")))
			.rhsOrNull("two")
			.assertEqualTo(
				type(
					pattern("one"), pattern("two" lineTo pattern("three")),
					pattern("three")))

		type(pattern("one"), pattern("two" lineTo rhs(recursion)))
			.rhsOrNull("two")
			.assertEqualTo(type(pattern("one"), pattern("two" lineTo rhs(recursion))))

		type(pattern("one"), pattern("two" lineTo rhs(recursion.recursion)))
			.rhsOrNull("two")
			.assertEqualTo(type(pattern("one")))
	}
}