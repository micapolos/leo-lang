package leo.base

import org.junit.Test

class StringTest {
	@Test
	fun byteStreamOrNull_empty() {
		"".byteStreamOrNull.assertEqualTo(null)
	}

	@Test
	fun byteStreamOrNull_nonEmpty() {
		"ab".byteStreamOrNull!!.assertContains(97, 98)
	}

	@Test
	fun byteStreamOrNull_utf8() {
		"ą".byteStreamOrNull!!.assertContains(-60, -123)
	}
}