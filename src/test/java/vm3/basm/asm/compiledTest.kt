package vm3.basm.asm

import leo.base.assertNotNull
import vm3.basm.block
import kotlin.test.Test

class InstrTest {
	@Test
	fun prim() {
		block()
			.compiled
			.assertNotNull
	}
}