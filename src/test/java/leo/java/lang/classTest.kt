package leo.java.lang

import leo.base.assertEqualTo
import leo.base.utf8String
import org.junit.Test

class ClassTest {
	@Test
	fun useResourceAsBitStream() {
		this::class.java
			.useResourceBitStreamOrNull("class.txt") { this?.utf8String }
			.assertEqualTo("test")
	}
}