package leo32.treo.util

import leo.base.assertEqualTo
import leo32.treo.variable
import kotlin.test.Test

class VecTest {
	@Test
	fun test32() {
		val vec32 = vec32 { variable() }

		vec32.varInt.assertEqualTo(0)
		vec32.varInt = 128
		vec32.varInt.assertEqualTo(128)

		vec32.varFloat = 3.14f
		vec32.varFloat.assertEqualTo(3.14f)
	}

	@Test
	fun test64() {
		val vec64 = vec64 { variable() }

		vec64.varLong.assertEqualTo(0)
		vec64.varLong = 128
		vec64.varLong.assertEqualTo(128)

		vec64.varDouble = 3.14
		vec64.varDouble.assertEqualTo(3.14)
	}
}