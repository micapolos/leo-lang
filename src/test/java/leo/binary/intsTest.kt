package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class IntsTest {
	@Test
	fun int() {
		int0.push(0.bit).int.assertEqualTo(0)
		int0.push(1.bit).int.assertEqualTo(1)

		int0.push(1.bit).push(0.bit).int.assertEqualTo(2)
		int0.push(1.bit).push(1.bit).int.assertEqualTo(3)

		int0.push(1.bit).push(0.bit).push(0.bit).int.assertEqualTo(4)
		int0.push(1.bit).push(0.bit).push(1.bit).int.assertEqualTo(5)

		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).int.assertEqualTo(8)
		int0.push(1.bit).push(0.bit).push(0.bit).push(1.bit).int.assertEqualTo(9)

		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(0.bit).int.assertEqualTo(16)
		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(1.bit).int.assertEqualTo(17)
	}

	@Test
	fun fromInt() {
		0.int1.assertEqualTo(int0.push(0.bit))
		1.int1.assertEqualTo(int0.push(1.bit))

		2.int2.assertEqualTo(int0.push(1.bit).push(0.bit))
		3.int2.assertEqualTo(int0.push(1.bit).push(1.bit))

		4.int3.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit))
		5.int3.assertEqualTo(int0.push(1.bit).push(0.bit).push(1.bit))

		8.int4.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit))
		9.int4.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(1.bit))

		16.int5.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(0.bit))
		17.int5.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(1.bit))
	}
}