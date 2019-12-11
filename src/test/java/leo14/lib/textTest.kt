package leo14.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class TextTest {
	@Test
	fun plus() {
		("Hello, ".text + "world!".text).assertEqualTo("Hello, world!".text)
	}
}