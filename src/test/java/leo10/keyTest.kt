package leo10

import leo.base.assertEqualTo
import leo.base.string
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class KeyTest {
	@Test
	fun string() {
		key(list(bit0, bit0, bit1, bit0)).string.assertEqualTo("key(0010)")
		"".key.string.assertEqualTo("key(00000000)")
		" ".key.string.assertEqualTo("key(0010000000000000)")
	}
}