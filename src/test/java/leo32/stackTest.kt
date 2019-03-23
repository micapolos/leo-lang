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
			.push(2)
			.push(3)
			.load(0)
			.op1 { times(2) }
			.op1 { inc() }
			.op2 { plus(it) }
			.op2 { times(it) }
			.assertEqualTo(0.stack.push(16))
	}
}