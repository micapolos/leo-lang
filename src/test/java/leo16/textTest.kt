package leo16

import leo.base.assertEqualTo
import kotlin.test.Test

class TextTest {
	@Test
	fun textString() {
		"Hello, ".text.plus("world!".text).string.assertEqualTo("Hello, world!")
	}
}