package leo3

import leo.base.assertEqualTo
import leo.base.empty
import leo.binary.bit
import leo.binary.one
import leo.binary.zero
import kotlin.test.Test

class ScopeTest {
	@Test
	fun emptyScope() {
		empty.scope.matchAt(bit(zero)).assertEqualTo(null)
		empty.scope.matchAt(bit(one)).assertEqualTo(null)
	}
}