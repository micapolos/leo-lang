package leo32.runtime

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class SymbolTest {
	@Test
	fun string() {
		assertFails { symbol("") }
		assertFails { symbol("\u0000") }
		symbol("foo").string.assertEqualTo("foo")
		symbol("bąk").string.assertEqualTo("bąk")
	}

	@Test
	fun equality() {
		symbol("foo").assertEqualTo(symbol("foo"))
	}
}