package leo32.base

import leo.base.assertEqualTo
import leo.base.clampedByte
import kotlin.test.Test

class ByteArrayTest {
	@Test
	fun fromIntArray() {
		intArray.byteArray
			.put(2, 0x01.clampedByte)
			.put(7, 0x01.clampedByte)
			.assertEqualTo(intArray.put(0, 256).put(1, 1).byteArray)
	}
}