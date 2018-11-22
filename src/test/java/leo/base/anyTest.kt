package leo.base

import kotlin.test.Test

class AnyTest {
	@Test
	fun useResourceBitStream() {
		this
			.useSiblingResourceBitStream("any.txt") { utf8String }
			.assertEqualTo("test")
	}
}