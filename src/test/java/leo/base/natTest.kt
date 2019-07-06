package leo.base

import leo.binary.zero
import kotlin.test.Test

class NatTest {
	@Test
	fun all() {
		0.nat.assertEqualTo(zero.nat)
		1.nat.assertEqualTo(zero.nat.inc)
		2.nat.assertEqualTo(zero.nat.inc.inc)

		zero.nat.int.assertEqualTo(0)
		zero.nat.inc.int.assertEqualTo(1)
		zero.nat.inc.inc.int.assertEqualTo(2)
	}
}