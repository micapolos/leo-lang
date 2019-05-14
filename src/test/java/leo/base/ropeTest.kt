package leo.base

import kotlin.test.Test

class RopeTest {
	@Test
	fun foldLarge() {
		val times = 1 shl 20
		val stack = stack(1).iterate(times - 1) { push(1) }
		val sum = 0.foldForward(rope(stack).moveFirst()) { plus(it) }
		sum.assertEqualTo(times)
	}
}