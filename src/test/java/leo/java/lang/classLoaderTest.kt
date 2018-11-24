package leo.java.lang

import leo.base.assertEqualTo
import leo.base.utf8String
import org.junit.Test

class ClassLoaderTest {
	@Test
	fun useResourceAsBitStream() {
		this::class.java.classLoader
			.useResourceBitStreamOrNull("test.txt") { this?.utf8String }
			.assertEqualTo("test")
	}
}