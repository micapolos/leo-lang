package leo.java.io

import leo.base.assertEqualTo
import leo.base.useSiblingResourceBitStreamOrNull
import leo.base.utf8String
import kotlin.test.Test

class FileTest {
	@Test
	fun useBitStream() {
		useSiblingResourceBitStreamOrNull("file.txt") { this?.utf8String }
			.assertEqualTo("test")
	}
}