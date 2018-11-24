package leo.base

import kotlin.test.Test

class AnyTest {
	@Test
	fun useResourceBitStream() {
		this
			.useSiblingResourceBitStreamOrNull("any.txt") { this?.utf8String }
			.assertEqualTo("test")
	}
}