package leo32.treo

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class VariableTest {
	@Test
	fun string() {
		variable().apply { bit = bit0 }.string.assertEqualTo("0")
		variable().apply { bit = bit1 }.string.assertEqualTo("1")
	}
}