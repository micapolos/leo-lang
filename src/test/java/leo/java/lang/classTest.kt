package leo.java.lang

import leo.base.assertEqualTo
import leo.base.utf8String
import org.junit.Test

class ClassTest {
	@Test
	fun useResourceAsBitStream() {
		this::class.java
			.useResourceBitStream("class.txt") { utf8String }
			.assertEqualTo("test")
	}
}