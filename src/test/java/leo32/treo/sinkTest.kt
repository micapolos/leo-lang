package leo32.treo

import leo.base.assertEqualTo
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class SinkTest {
	@Test
	fun put() {
		sinkString {
			put(bit0)
			put(bit1)
			put(bit0)
			put(bit0)
		}.assertEqualTo("0100")
	}
}