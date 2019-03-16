package leo32

import leo.base.assertEqualTo
import leo.binary.bit
import leo.binary.zero
import kotlin.test.Test

class ScopeTest {
	@Test
	fun pushBit() {
		stackRuntime
			.scope
			.push(zero.bit)
			.assertEqualTo(null)
	}
}