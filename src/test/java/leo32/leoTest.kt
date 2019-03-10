package leo32

import leo.base.assertEqualTo
import leo.binary.oneBit
import leo.binary.zeroBit
import kotlin.test.Test

class LeoTest {
	@Test
	fun add() {
		emptyLeo
			.push(zeroBit)
			.push(oneBit)
			.code
			.assertEqualTo(12)
	}
}