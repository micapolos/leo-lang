package lambda.v2

import leo.base.assertEqualTo
import org.junit.Test

class NatTest {
	@Test
	fun test() {
		zeroNat(id, id).assertEqualTo(id)
		natSucc(zeroNat)(id, id).assertEqualTo(zeroNat)
		natSucc(natSucc(zeroNat))(id, id).assertEqualTo(natSucc(zeroNat))
	}
}