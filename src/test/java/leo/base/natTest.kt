package leo.base

import leo.binary.zero
import kotlin.test.Test

class NatTest {
	@Test
	fun all() {
		0.nat.assertEqualTo(zero.nat)
		1.nat.assertEqualTo(zero.nat.inc)
		2.nat.assertEqualTo(zero.nat.inc.inc)
	}
}