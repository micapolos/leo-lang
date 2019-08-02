package leo10

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class ListTest {
	@Test
	fun string() {
		list<Int>().string.assertEqualTo("[]")
		list(1).string.assertEqualTo("[1]")
		list(1, 2, 3).string.assertEqualTo("[1, 2, 3]")
	}
}