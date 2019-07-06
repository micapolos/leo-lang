package lambda.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class NatTest {
	@Test
	fun equality() {
		zeroNat.natDecOrZero.assertEqualTo(zeroNat)
		zeroNat.natInc.natDecOrZero.assertEqualTo(zeroNat)
		zeroNat.natInc.natInc.natDecOrZero.assertEqualTo(zeroNat.natInc)
	}
}