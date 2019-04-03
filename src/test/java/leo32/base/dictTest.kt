package leo32.base

import leo.base.assertContains
import leo.base.assertEqualTo
import kotlin.test.Test

class DictTest {
	@Test
	fun stringDictKey() {
		"foo".dictKey.bitSeq.assertContains()
	}
}