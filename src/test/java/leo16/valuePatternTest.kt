package leo16

import leo.base.assertEqualTo
import leo16.names.*
import kotlin.test.Test

class ValuePatternTest {
	@Test
	fun empty() {
		value().pattern.assertEqualTo(emptyPattern)
	}

	@Test
	fun any() {
		_any().value.pattern.assertEqualTo(anyPattern)
	}

	@Test
	fun anyTail() {
		value(_any(), _zero()).pattern.assertEqualTo(anyPattern.plus(_zero.invoke(emptyPattern)))
	}
}