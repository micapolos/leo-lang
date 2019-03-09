package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class LeoTest {
	@Test
	fun add() {
		emptyLeo
			.push(1)
			.push(2)
			.code
			.assertEqualTo(12)
	}
}