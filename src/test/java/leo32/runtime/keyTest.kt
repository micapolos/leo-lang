package leo32.runtime

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class KeyTest {
	@Test
	fun string() {
		key("").string.assertEqualTo("")
		key("foo").string.assertEqualTo("foo")
		key("bąk").string.assertEqualTo("bąk")
		key("\u0000").string.assertEqualTo("\u0000")
	}
}