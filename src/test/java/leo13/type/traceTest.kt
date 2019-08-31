package leo13.type

import leo.base.assertEqualTo
import leo13.compiler.applyOrNull
import leo13.compiler.lhsOrNull
import leo13.compiler.rhsOrNull
import leo13.compiler.trace
import kotlin.test.Test

class TraceTest {
	@Test
	fun applyThunk() {
		trace(type("one"), type("two"))
			.applyOrNull(thunk(type("three")))
			.assertEqualTo(trace(type("one"), type("two"), type("three")))

		trace(type("one"), type("two"), type("three"))
			.applyOrNull(thunk(recursion))
			.assertEqualTo(trace(type("one"), type("two")))

		trace(type("one"), type("two"), type("three"))
			.applyOrNull(thunk(recursion.recursion))
			.assertEqualTo(trace(type("one")))

		trace(type("one"), type("two"), type("three"))
			.applyOrNull(thunk(recursion.recursion.recursion))
			.assertEqualTo(null)
	}

	@Test
	fun lhsOrNull() {
		trace(
			type("foo"),
			type())
			.lhsOrNull
			.assertEqualTo(null)

		trace(
			type("foo"),
			type("two"))
			.lhsOrNull
			.assertEqualTo(
				trace(
					type("foo"),
					type()))

		trace(
			type("foo"),
			type(
				"one" lineTo type(),
				"two" lineTo type()))
			.lhsOrNull
			.assertEqualTo(
				trace(
					type("foo"),
					type("one")))
	}

	@Test
	fun rhsOrNull() {
		trace(
			type("foo"),
			type())
			.rhsOrNull
			.assertEqualTo(null)

		trace(
			type("foo"),
			type("one"))
			.rhsOrNull
			.assertEqualTo(
				trace(
					type("foo"),
					type("one"),
					type()))

		trace(
			type("foo"),
			type("one" lineTo type("two")))
			.rhsOrNull
			.assertEqualTo(
				trace(
					type("foo"),
					type("one" lineTo type("two")),
					type("two")))
	}

	@Test
	fun nameRhsOrNull() {
		trace(type("one"), type("two" lineTo type("three")))
			.rhsOrNull("two")
			.assertEqualTo(
				trace(
					type("one"), type("two" lineTo type("three")),
					type("three")))

		trace(type("one"), type("two" lineTo thunk(recursion)))
			.rhsOrNull("two")
			.assertEqualTo(trace(type("one")))

		trace(type("one"), type("two" lineTo thunk(recursion)))
			.rhsOrNull("two")
			.assertEqualTo(trace(type("one")))
	}
}