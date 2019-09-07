package leo13.base

import leo.base.assertEqualTo
import kotlin.test.Test

class TextTest {
	@Test
	fun type() {
		text(" ").assertEqualTo()
	}
}