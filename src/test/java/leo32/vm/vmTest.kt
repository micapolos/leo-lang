package leo32.vm

import kotlin.test.Test

class VmTest {
	@Test
	fun plus() {
		pushOp(i32plusOp)
	}
}
