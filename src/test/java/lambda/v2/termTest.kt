package lambda.v2

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class TermTest {
	@Test
	fun string() {
		fn { a0 }.string.assertEqualTo("fn { a0 }")
		fn { fn { a1 } }.string.assertEqualTo("fn { fn { a1 } }")
		fn { fn { a0 } }.string.assertEqualTo("fn { fn { a0 } }")
		a0.string.assertEqualTo("a0")
		a0(a1).string.assertEqualTo("a0(a1)")
		fn { a0 }.apply(fn { a1 }).string.assertEqualTo("fn { a0 }(fn { a1 })")
	}

	@kotlin.test.Test
	fun functionTermOrNull() {
		fn(3){ id }.functionTermOrNull(3).assertEqualTo(id)
	}
}