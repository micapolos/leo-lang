package leo32

import leo.base.assertEqualTo
import leo.binary.*
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun pushBit() {
		emptyRuntime
			.push(zero.bit)
			.assertEqualTo(
				Runtime(
					emptyFunction,
					emptyStack32<Bit>().push(zero.bit)!!,
					emptyFunction))
	}
}