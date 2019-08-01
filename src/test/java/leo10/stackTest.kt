package leo10

import leo.base.assertEqualTo
import leo.base.string
import leo9.stack
import kotlin.test.Test

class StackTest {
	@Test
	fun string() {
		stack<Int>().string.assertEqualTo("stack")
		stack(1).string.assertEqualTo("stack.push(1)")
		stack(1, 2, 3).string.assertEqualTo("stack.push(1).push(2).push(3)")
	}
}