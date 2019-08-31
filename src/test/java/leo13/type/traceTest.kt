package leo13.type

import leo.base.assertEqualTo
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
	fun rhsOrNull() {
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