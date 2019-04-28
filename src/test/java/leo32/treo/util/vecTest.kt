package leo32.treo.util

import leo.base.assertEqualTo
import leo32.treo.variable
import kotlin.test.Test

class VecTest {
	@Test
	fun test32() {
		val vec32 = vec32 { variable() }

		vec32.int.assertEqualTo(0)
		vec32.int = 128
		vec32.int.assertEqualTo(128)

		vec32.float = 3.14f
		vec32.float.assertEqualTo(3.14f)
	}

	@Test
	fun test64() {
		val vec64 = vec64 { variable() }

		vec64.long.assertEqualTo(0)
		vec64.long = 128
		vec64.long.assertEqualTo(128)

		vec64.double = 3.14
		vec64.double.assertEqualTo(3.14)
	}
}