package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class VariableTest {
	@Test
	fun string() {
		variable().apply { set(bit0) }.string.assertEqualTo("_0")
		variable().apply { set(bit1) }.string.assertEqualTo("_1")
	}
}