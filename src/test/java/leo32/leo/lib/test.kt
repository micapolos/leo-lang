package leo32.leo.lib

import leo32.leo._test
import kotlin.test.Test

class Test {
	@Test
	fun test() {
		_test(coreLib)
		_test(bitLib)
		_test(byteLib)
	}
}