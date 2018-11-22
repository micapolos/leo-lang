package leo.java.io

import leo.base.assertEqualTo
import leo.base.useSiblingResourceBitStream
import leo.base.utf8String
import kotlin.test.Test

class FileTest {
	@Test
	fun useBitStream() {
		useSiblingResourceBitStream("file.txt") { utf8String }
			.assertEqualTo("test")
	}
}