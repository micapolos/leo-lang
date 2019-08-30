package leo13.type

import leo.base.assertEqualTo
import kotlin.test.Test

class TypeTraceTest {
	@Test
	fun resolve() {
		type("one" lineTo type("two" lineTo type("three" lineTo thunk(recursion.recursion))))
			.trace
			.run {
				rhsOrNull("one")
					.assertEqualTo(
						trace(
							type("one" lineTo type("two" lineTo type("three" lineTo thunk(recursion.recursion)))),
							type("two" lineTo type("three" lineTo thunk(recursion.recursion)))))

				rhsOrNull("one")!!
					.rhsOrNull("two")
					.assertEqualTo(
						trace(
							type("one" lineTo type("two" lineTo type("three" lineTo thunk(recursion.recursion)))),
							type("two" lineTo type("three" lineTo thunk(recursion.recursion))),
							type("three" lineTo thunk(recursion.recursion))))

				rhsOrNull("one")!!
					.rhsOrNull("two")!!
					.rhsOrNull("three")
					.assertEqualTo(
						trace(
							type("one" lineTo type("two" lineTo type("three" lineTo thunk(recursion.recursion)))),
							type("two" lineTo type("three" lineTo thunk(recursion.recursion)))))
			}
	}
}