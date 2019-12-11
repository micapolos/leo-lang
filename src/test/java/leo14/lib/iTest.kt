package leo14.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class ITest {
	@Test
	fun ints() {
		1.i2.int.assertEqualTo(1)
		12.i4.int.assertEqualTo(12)
		123.i8.int.assertEqualTo(123)
		12345.i16.int.assertEqualTo(12345)
		12345678.i32.int.assertEqualTo(12345678)
		1234567890123456789L.i64.long.assertEqualTo(1234567890123456789L)
	}
}