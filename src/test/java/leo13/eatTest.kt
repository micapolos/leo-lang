package leo13

import leo.base.assertEqualTo
import kotlin.test.Test

class EatTest {
	@Test
	fun intAdd() {
		intInc eat 1 assertEqualTo 2
		intAdd eat 1 eat 2 assertEqualTo 3
		listAt eat listOf("foo", "bar") eat 1 assertEqualTo "bar"
		intInc dot intInc dot intInc eat 1 assertEqualTo 4
	}
}