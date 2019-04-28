package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class VarTest {
	@Test
	fun string() {
		newVar().apply { set(bit0) }.string.assertEqualTo("_0")
		newVar().apply { set(bit1) }.string.assertEqualTo("_1")
	}
}