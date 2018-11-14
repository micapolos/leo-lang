package leo.base

import kotlin.test.Test

class ByteArrayTest {
	@Test
	fun streamOrNull_empty() {
		byteArrayOf().streamOrNull.assertEqualTo(null)
	}

	@Test
	fun streamOrNull_nonEmpty() {
		byteArrayOf(1, 2, 3).streamOrNull!!.assertContains(1, 2, 3)
	}
}