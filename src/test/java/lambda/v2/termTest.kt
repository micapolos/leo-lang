package lambda.v2

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		fn { arg(0) }.string.assertEqualTo("x0 -> x0")
		fn { fn { arg(0) } }.string.assertEqualTo("x0 -> x1 -> x1")
		fn { fn { arg(1) } }.string.assertEqualTo("x0 -> x1 -> x0")
		arg(0).string.assertEqualTo("?1")
		arg(0)(arg(1)).string.assertEqualTo("?1(?2)")
	}

	@kotlin.test.Test
	fun functionTermOrNull() {
		fn { fn { fn { id } } }.functionTermOrNull(3).assertEqualTo(id)
	}
}