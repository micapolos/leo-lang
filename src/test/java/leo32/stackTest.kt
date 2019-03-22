package leo32

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class StackTest {
	@Test
	fun string() {
		0.stack.string.assertEqualTo("0.stack")
		0.stack.push(10).push(20).string.assertEqualTo("0.stack.push(10).push(20)")
	}

	@Test
	fun ops() {
		0.stack
			.push(10)
			.push(20)
			.op2 { plus(it) }
			.assertEqualTo(0.stack.push(30))
	}
}